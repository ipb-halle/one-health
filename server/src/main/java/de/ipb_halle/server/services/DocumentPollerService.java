package de.ipb_halle.server.services;

import de.ipb_halle.server.postgre.models.DocumentRecord;
import de.ipb_halle.server.postgre.repositories.DocumentRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that polls for PENDING documents and dispatches them to the worker.
 */
@Service
public class DocumentPollerService {

    private static final Logger log = LoggerFactory.getLogger(DocumentPollerService.class);

    private final DocumentRecordRepository documentRepository;
    private final DocumentWorkerService documentWorkerService;
    private final int batchSize;

    public DocumentPollerService(DocumentRecordRepository documentRepository,
                                 DocumentWorkerService documentWorkerService,
                                 @Value("${document.poller.batch-size}") int batchSize) {
        this.documentRepository = documentRepository;
        this.documentWorkerService = documentWorkerService;
        this.batchSize = batchSize;
    }

    /**
     * Polls for PENDING documents and dispatches them to workers.
     * 
     * Runs periodically based on the configured fixed delay. Fetches a batch of
     * PENDING documents in FIFO order.
     * 
     * Each document is dispatched to an async worker. If any individual document
     * fails to dispatch, the error is logged and processing continues with the
     * next document.
     */
    @Scheduled(fixedDelayString = "${document.poller.delay-ms}")
    public void pollAndDispatch() {
        try {
            // Fetch a batch of PENDING documents
            List<DocumentRecord> pendingDocuments = documentRepository.findPendingDocumentsForProcessing(batchSize);
            
            if (pendingDocuments.isEmpty()) {
                log.debug("No PENDING documents found in this poll cycle");
                return;
            }

            log.info("Fetched {} PENDING documents for processing", pendingDocuments.size());

            // Dispatch each document to the worker thread pool
            for (DocumentRecord document : pendingDocuments) {
                try {
                    log.debug("Dispatching document {} to worker", document.getId());
                    documentWorkerService.process(document.getId());
                } catch (Exception ex) {
                    // Log the error but continue with the next document
                    log.error("Failed to dispatch document {} to worker: {}", 
                        document.getId(), ex.getMessage(), ex);
                }
            }
        } catch (Exception ex) {
            log.error("Error in document polling cycle: {}", ex.getMessage(), ex);
        }
    }
}
