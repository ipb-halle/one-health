package de.ipb_halle.server.services;

import de.ipb_halle.server.postgre.models.DocumentRecord;
import de.ipb_halle.server.postgre.models.DocumentRecordStatus;
import de.ipb_halle.server.postgre.repositories.DocumentRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
public class DocumentUploadService {

    private static final Logger log = LoggerFactory.getLogger(DocumentUploadService.class);

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "application/pdf",
        "text/csv",
        "text/plain"
    );

    private static final String TEMP_FILE_PREFIX = "doc_";

    private final DocumentRecordRepository documentRecordRepository;

    @Autowired
    public DocumentUploadService(DocumentRecordRepository documentRecordRepository) {
        this.documentRecordRepository = documentRecordRepository;
    }

    /**
     * Uploads a file by streaming it directly to a temporary directory
     * and creating a DocumentRecord with PENDING status.
     *
     * @param file the multipart file to upload
     * @return DocumentRecord with the temporary file path and PENDING status
     * @throws IllegalArgumentException if file is invalid
     * @throws IOException if an I/O error occurs during file transfer
     */
    @Transactional
    public DocumentRecord uploadFile(MultipartFile file) throws IOException {
        // Validate file
        validateFile(file);

        // Stream file to temp directory
        Path tempFilePath = streamFileToTemp(file);

        // Create and save DocumentRecord
        DocumentRecord documentRecord = new DocumentRecord(
            file.getOriginalFilename(),
            tempFilePath.toString(),
            DocumentRecordStatus.PENDING
        );

        DocumentRecord savedRecord = documentRecordRepository.save(documentRecord);
        log.info("DocumentRecord created with ID: {} for file: {}", 
            savedRecord.getId(), file.getOriginalFilename());

        return savedRecord;
    }

    /**
     * Validates the uploaded file for emptiness and allowed MIME types.
     *
     * @param file the multipart file to validate
     * @throws IllegalArgumentException if file is invalid
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            log.warn("Upload attempted with empty file");
            throw new IllegalArgumentException("File cannot be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            log.warn("Upload attempted with disallowed MIME type: {}", contentType);
            throw new IllegalArgumentException(
                "File type not allowed. Supported types: PDF, CSV, TXT"
            );
        }

        log.debug("File validation passed for: {}", file.getOriginalFilename());
    }

    /**
     * Streams the multipart file directly to a temporary directory
     * without loading it entirely into memory.
     *
     * @param file the multipart file to stream
     * @return Path to the created temporary file
     * @throws IOException if an I/O error occurs during file transfer
     */
    private Path streamFileToTemp(MultipartFile file) throws IOException {
        try {
            // Determine file extension from original filename
            String extension = getFileExtension(file.getOriginalFilename());
            
            // Create temporary file with prefix and suffix
            Path tempFilePath = Files.createTempFile(TEMP_FILE_PREFIX, extension);
            
            // Stream the file content directly to disk
            file.transferTo(tempFilePath.toFile());
            
            log.debug("File streamed to temporary location: {}", tempFilePath);
            return tempFilePath;
        } catch (IOException e) {
            log.error("Error streaming file to temporary directory: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Extracts file extension from filename.
     * Returns empty string if no extension found.
     *
     * @param filename the original filename
     * @return the file extension (including dot) or empty string
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filename.length() - 1) {
            return "." + filename.substring(lastDot + 1);
        }
        return "";
    }
}
