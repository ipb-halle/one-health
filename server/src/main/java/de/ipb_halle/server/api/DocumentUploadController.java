package de.ipb_halle.server.api;

import de.ipb_halle.server.data.dtos.DocumentUploadResponseDTO;
import de.ipb_halle.server.postgre.models.DocumentRecord;
import de.ipb_halle.server.services.DocumentUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/documents")
@CrossOrigin(origins = "*")
public class DocumentUploadController {

    private static final Logger log = LoggerFactory.getLogger(DocumentUploadController.class);

    private final DocumentUploadService documentUploadService;

    @Autowired
    public DocumentUploadController(DocumentUploadService documentUploadService) {
        this.documentUploadService = documentUploadService;
    }

    /**
     * Handles multipart file uploads via streaming.
     * 
     * Files are streamed directly to a secure temporary directory without
     * loading the entire file into memory. A DocumentRecord is created with
     * PENDING status and immediately returned with HTTP 202 Accepted.
     *
     * @param file the uploaded file
     * @return ResponseEntity with HTTP 202 Accepted and document metadata
     */
    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponseDTO> uploadDocument(
            @RequestParam("file") MultipartFile file) {
        try {
            log.info("Document upload initiated for file: {}", file.getOriginalFilename());

            // Upload file and create database record
            DocumentRecord documentRecord = documentUploadService.uploadFile(file);

            // Create response DTO
            DocumentUploadResponseDTO response = new DocumentUploadResponseDTO(
                documentRecord.getId(),
                documentRecord.getStatus().toString(),
                "Document uploaded successfully and queued for processing"
            );

            log.info("Document uploaded successfully with ID: {}", documentRecord.getId());
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

        } catch (IllegalArgumentException e) {
            log.warn("Validation error during document upload: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (IOException e) {
            log.error("IO error during document upload: {}", e.getMessage(), e);
            return buildErrorResponse(
                "Failed to process file upload. Please try again.",
                HttpStatus.INTERNAL_SERVER_ERROR
            );

        } catch (Exception e) {
            log.error("Unexpected error during document upload: {}", e.getMessage(), e);
            return buildErrorResponse(
                "An unexpected error occurred during upload",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Helper method to build error response DTOs.
     *
     * @param message the error message
     * @param status the HTTP status
     * @return ResponseEntity with error details
     */
    private ResponseEntity<DocumentUploadResponseDTO> buildErrorResponse(
            String message,
            HttpStatus status) {
        DocumentUploadResponseDTO errorResponse = new DocumentUploadResponseDTO(
            null,
            "ERROR",
            message
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
