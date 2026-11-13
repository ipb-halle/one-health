package de.ipb_halle.server.postgre.repositories;

import de.ipb_halle.server.postgre.models.DocumentRecord;
import de.ipb_halle.server.postgre.models.DocumentRecordStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRecordRepository extends JpaRepository<DocumentRecord, Long> {

    /**
     * Find all documents with a specific status.
     */
    List<DocumentRecord> findByStatus(DocumentRecordStatus status);

    /**
     * Fetch PENDING documents
     */
    @Query(value = "SELECT * FROM document_records WHERE status = 'PENDING' ORDER BY created_at ASC LIMIT :batchSize", nativeQuery = true)
    List<DocumentRecord> findPendingDocumentsForProcessing(@Param("batchSize") int batchSize);
}
