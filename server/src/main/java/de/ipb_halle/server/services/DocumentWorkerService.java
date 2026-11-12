package de.ipb_halle.server.services;

import de.ipb_halle.server.postgre.models.DocumentRecord;
import de.ipb_halle.server.postgre.models.DocumentRecordStatus;
import de.ipb_halle.server.postgre.repositories.DocumentRecordRepository;
import de.ipb_halle.server.services.client.DownstreamConsumerClient;
import de.ipb_halle.server.services.client.dto.ExtractionResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Service for asynchronous document processing.
 * 
 * Processes documents end-to-end: fetches from disk, sends to downstream
 * extraction service, and stores results. Uses semaphore to serialize
 * access to the downstream service (which has limited concurrency).
 */
@Service
public class DocumentWorkerService {

    private static final Logger log = LoggerFactory.getLogger(DocumentWorkerService.class);

    private final DocumentRecordRepository documentRepository;
    private final DownstreamConsumerClient downstreamConsumerClient;
    private final Semaphore downstreamPermit;
    private final ObjectMapper objectMapper;
    private final int downstreamMaxWaitSeconds;

    public DocumentWorkerService(DocumentRecordRepository documentRepository,
                                 DownstreamConsumerClient downstreamConsumerClient,
                                 ObjectMapper objectMapper,
                                 @Value("${document.downstream.max-wait-seconds}") int downstreamMaxWaitSeconds) {
        this.documentRepository = documentRepository;
        this.downstreamConsumerClient = downstreamConsumerClient;
        // Semaphore(1) ensures only one document is processed at a time by the downstream service
        this.downstreamPermit = new Semaphore(1);
        this.objectMapper = objectMapper;
        this.downstreamMaxWaitSeconds = downstreamMaxWaitSeconds;
    }

    /**
     * Processes a single document asynchronously end-to-end.
     * 
     * Pipeline:
     * 1. Load document from database; skip if not found or not in PENDING status
     * 2. Update status to DISPATCHED
     * 3. Acquire semaphore (300s timeout) to serialize downstream access
     * 4. Consume via downstream service, store result as JSON
     * 5. Mark as CONSUMED on success
     * 6. On error: increment retry count and mark as FAILED
     * 7. Always cleanup: delete the local file (with error logging that doesn't mask exceptions)
     *
     * @param documentId the ID of the document to process
     */
    @Async("documentWorkerExecutor")
    public void process(Long documentId) {
        // 1. Load the document
        DocumentRecord document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            log.warn("Document not found with id: {}", documentId);
            return;
        }

        if (document.getStatus() != DocumentRecordStatus.PENDING) {
            log.warn("Document {} is not in PENDING status, current status: {}", documentId, document.getStatus());
            return;
        }

        try {
            // 2. Update status to DISPATCHED and persist
            document.setStatus(DocumentRecordStatus.DISPATCHED);
            document.setUpdatedAt(LocalDateTime.now());
            documentRepository.save(document);
            log.debug("Document {} status updated to DISPATCHED", documentId);

            // 3. Acquire semaphore and process
            if (!downstreamPermit.tryAcquire(downstreamMaxWaitSeconds, TimeUnit.SECONDS)) {
                log.error("Failed to acquire downstream permit for document {} within {} seconds timeout", documentId, downstreamMaxWaitSeconds);
                document.setStatus(DocumentRecordStatus.FAILED);
                document.setUpdatedAt(LocalDateTime.now());
                documentRepository.save(document);
                return;
            }

            try {
                // Consume document via downstream extraction service
                ExtractionResult response = downstreamConsumerClient.consume(
                    document.getFilePath(),
                    document.getOriginalFilename()
                );

                // Store the extraction result as JSON
                String resultJson = objectMapper.writeValueAsString(response);
                document.setResultJson(resultJson);

                // Mark document as successfully consumed
                document.setStatus(DocumentRecordStatus.CONSUMED);
                document.setUpdatedAt(LocalDateTime.now());
                documentRepository.save(document);
                log.info("Document {} successfully processed and marked as CONSUMED", documentId);
            } finally {
                // Always release the semaphore permit
                downstreamPermit.release();
                log.debug("Released downstream permit for document {}", documentId);
            }
        } catch (Exception ex) {
            // 4. On any exception: increment retry count and mark as FAILED
            log.error("Error processing document {}: {}", documentId, ex.getMessage(), ex);
            document.setRetryCount(document.getRetryCount() + 1);
            document.setStatus(DocumentRecordStatus.FAILED);
            document.setUpdatedAt(LocalDateTime.now());
            try {
                documentRepository.save(document);
                log.debug("Document {} marked as FAILED with retry count: {}", documentId, document.getRetryCount());
            } catch (Exception saveEx) {
                log.error("Failed to persist document {} error state after exception", documentId, saveEx);
            }
        } finally {
            // 5. Cleanup: delete the local file (wrap in try/catch to never mask the original exception)
            try {
                Files.deleteIfExists(Paths.get(document.getFilePath()));
                log.debug("Deleted file: {}", document.getFilePath());
            } catch (IOException ioEx) {
                log.error("Failed to delete file {}: {}", document.getFilePath(), ioEx.getMessage(), ioEx);
            }
        }
    }
}
