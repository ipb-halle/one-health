package de.ipb_halle.server.data.dtos;

public class ReferenceDTO {
    private String source;
    private String externalId;
    private String sourceUrl;
    private String entityId;

    public ReferenceDTO() {
    }

    public ReferenceDTO(String source, String externalId, String sourceUrl, String entityId) {
        this.source = source;
        this.externalId = externalId;
        this.sourceUrl = sourceUrl;
        this.entityId = entityId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
