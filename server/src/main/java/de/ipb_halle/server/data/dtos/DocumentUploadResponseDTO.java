package de.ipb_halle.server.data.dtos;

public class DocumentUploadResponseDTO {
    private Long documentId;
    private String status;
    private String message;

    public DocumentUploadResponseDTO() {
    }

    public DocumentUploadResponseDTO(Long documentId, String status, String message) {
        this.documentId = documentId;
        this.status = status;
        this.message = message;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
