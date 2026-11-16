package de.ipb_halle.server.services.client;

import de.ipb_halle.server.services.client.dto.ExtractionRequest;
import de.ipb_halle.server.services.client.dto.ExtractionResult;
import de.ipb_halle.server.services.exceptions.DownstreamConsumptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Client for consuming documents via the downstream FastAPI extraction service.
 * Handles Base64 encoding, JSON request payload, and retry/recovery logic.
 */
@Component
public class DownstreamConsumerClient {

    private static final Logger log = LoggerFactory.getLogger(DownstreamConsumerClient.class);

    private final RestClient restClient;

    public DownstreamConsumerClient(RestClient downstreamRestClient) {
        this.restClient = downstreamRestClient;
    }

    /**
     * Consumes a document file by Base64 encoding it and sending it as JSON
     * to the downstream FastAPI extraction service.
     *
     * Retries up to 3 times with exponential backoff on network or
     * response errors. Falls back to recovery method which logs and rethrows as
     * DownstreamConsumptionException.
     *
     * @param filePath the absolute path to the file on disk
     * @param fileName the original file name to send with the request
     * @return ExtractionResult containing extracted compounds
     * @throws DownstreamConsumptionException if all retries are exhausted
     */
    @Retryable(
            retryFor = {ResourceAccessException.class, RestClientResponseException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public ExtractionResult consume(String filePath, String fileName) {
        log.debug("Sending document for extraction (Base64 JSON) to FastAPI service: {}", fileName);

        try {
            // 1. Read raw bytes from disk and encode to Base64 string
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String fileBase64 = Base64.getEncoder().encodeToString(fileBytes);

            // 2. Build the request DTO matching the FastAPI Base64Bytes schema
            ExtractionRequest payload = new ExtractionRequest(fileName, fileBase64);

            // 3. Execute POST request with application/json
            return restClient.post()
                    .uri("/api/v1/extract")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(ExtractionResult.class);

        } catch (IOException e) {
            log.error("Failed to read or Base64-encode file for downstream extraction: {}", filePath, e);
            throw new ResourceAccessException("Failed to process file bytes for downstream request", e);
        }
    }

    /**
     * Recovery method invoked after all retry attempts are exhausted.
     * Logs the final failure and throws a custom DownstreamConsumptionException.
     *
     * @param ex the exception that triggered recovery
     * @param filePath the file path that failed
     * @param fileName the file name that failed
     * @throws DownstreamConsumptionException always
     */
    @Recover
    public ExtractionResult recover(Exception ex, String filePath, String fileName) {
        String errorMessage = String.format(
                "Downstream consumption failed for file %s (path: %s) after retries",
                fileName, filePath
        );
        log.error(errorMessage, ex);
        throw new DownstreamConsumptionException(errorMessage, ex);
    }
}