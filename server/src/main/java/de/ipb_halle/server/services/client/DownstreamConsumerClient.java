package de.ipb_halle.server.services.client;

import de.ipb_halle.server.services.client.dto.ExtractionResult;
import de.ipb_halle.server.services.exceptions.DownstreamConsumptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Client for consuming documents via the downstream extraction service.
 * Handles multipart file uploads with retry and recovery logic.
 */
@Component
public class DownstreamConsumerClient {

    private static final Logger log = LoggerFactory.getLogger(DownstreamConsumerClient.class);

    private final WebClient webClient;

    public DownstreamConsumerClient(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Consumes a document file by sending it to the downstream extraction service.
     * 
     * Retries up to 3 times with exponential backoff (1s, 2s, 4s) on network or
     * response errors. Falls back to recovery method which logs and rethrows as
     * DownstreamConsumptionException.
     *
     * @param filePath the absolute path to the file on disk
     * @param fileName the original file name to send with the request
     * @return ExtractionResult containing extracted compounds
     * @throws DownstreamConsumptionException if all retries are exhausted
     */
    @Retryable(
            retryFor = {WebClientRequestException.class, WebClientResponseException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public ExtractionResult consume(String filePath, String fileName) {
        log.debug("Sending document for extraction: {}", fileName);

        FileSystemResource fileResource = new FileSystemResource(filePath);

        return webClient.post()
                .uri("/api/v1/consume")
                .body(BodyInserters.fromMultipartData(buildMultipartBody(fileResource, fileName)))
                .retrieve()
                .bodyToMono(ExtractionResult.class)
                .doOnError(error -> log.warn("Error consuming document {}: {}", fileName, error.getMessage()))
                .block();
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

    /**
     * Builds the multipart request body with file and metadata.
     *
     * @param fileResource the file resource to include
     * @param fileName the original file name
     * @return MultiValueMap representing the multipart body
     */
    private MultiValueMap<String, Object> buildMultipartBody(FileSystemResource fileResource, String fileName) {
        MultiValueMap<String, Object> body = new org.springframework.util.LinkedMultiValueMap<>();
        
        // Add the file part with the name "file" and original filename
        body.add("file", fileResource);
        body.add("fileName", fileName);
        
        return body;
    }
}
