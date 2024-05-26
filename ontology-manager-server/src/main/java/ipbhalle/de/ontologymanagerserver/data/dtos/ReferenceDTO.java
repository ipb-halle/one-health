package ipbhalle.de.ontologymanagerserver.data.dtos;

public class ReferenceDTO {
    private String source;
    private String externalId;
    private String sourceUrl;

    public ReferenceDTO(){}

    public ReferenceDTO(String source, String externalId, String sourceUrl){
        this.source = source;
        this.externalId = externalId;
        this.sourceUrl = sourceUrl;
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
}
