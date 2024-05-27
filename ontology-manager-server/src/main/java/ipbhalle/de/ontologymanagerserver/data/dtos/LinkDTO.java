package ipbhalle.de.ontologymanagerserver.data.dtos;
import java.util.List;

public class LinkDTO {
    private String leftEntity;
    private String rightEntity;
    private String type;
    private List<PropertyValueDTO> properties;
    private String sourceName;
    private String sourceUrl;

    public LinkDTO(){}

    public String getLeftEntity() {
        return leftEntity;
    }


    public void setLeftEntity(String leftEntity) {
        this.leftEntity = leftEntity;
    }

    public String getRightEntity() {
        return rightEntity;
    }

    public void setRightEntity(String rightEntity) {
        this.rightEntity = rightEntity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PropertyValueDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyValueDTO> properties) {
        this.properties = properties;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
