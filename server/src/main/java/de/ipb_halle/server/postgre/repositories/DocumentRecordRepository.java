package de.ipb_halle.server.postgre.repositories;

import de.ipb_halle.server.postgre.models.DocumentRecord;
import de.ipb_halle.server.postgre.models.DocumentRecordStatus;
import java.time.LocalDateTime;
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
     * Find all documents created after a given timestamp.
     */
    List<DocumentRecord> findByCreatedAtAfter(LocalDateTime createdAt);

    /**
     * Find all documents by status and created after a given timestamp.
     */
    List<DocumentRecord> findByStatusAndCreatedAtAfter(DocumentRecordStatus status, LocalDateTime createdAt);

    /**
     * Custom query to find failed documents older than a given date.
     * Useful for cleanup operations.
     */
    @Query("SELECT dr FROM DocumentRecord dr WHERE dr.status = :status AND dr.updatedAt < :beforeDate")
    List<DocumentRecord> findOldDocumentsByStatus(
        @Param("status") DocumentRecordStatus status,
        @Param("beforeDate") LocalDateTime beforeDate
    );
}
