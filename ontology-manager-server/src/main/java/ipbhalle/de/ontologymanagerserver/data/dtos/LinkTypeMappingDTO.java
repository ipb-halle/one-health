package ipbhalle.de.ontologymanagerserver.data.dtos;

import java.util.HashMap;

public class LinkTypeMappingDTO {
    private String type;
    private String leftEntityTypeKey;
    private String leftEntityTypeKeyMapping;
    private String rightEntityTypeKey;
    private String rightEntityTypeKeyMapping;
    private HashMap<String, String> properties;

    public LinkTypeMappingDTO() {
    }

    public LinkTypeMappingDTO(String type, String leftEntityTypeKey, String leftEntityTypeKeyMapping, String rightEntityTypeKey, String rightEntityTypeKeyMapping, HashMap<String, String> properties) {
        this.type = type;
        this.leftEntityTypeKey = leftEntityTypeKey;
        this.leftEntityTypeKeyMapping = leftEntityTypeKeyMapping;
        this.rightEntityTypeKey = rightEntityTypeKey;
        this.rightEntityTypeKeyMapping = rightEntityTypeKeyMapping;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeftEntityTypeKey() {
        return leftEntityTypeKey;
    }

    public void setLeftEntityTypeKey(String leftEntityTypeKey) {
        this.leftEntityTypeKey = leftEntityTypeKey;
    }

    public String getLeftEntityTypeKeyMapping() {
        return leftEntityTypeKeyMapping;
    }

    public void setLeftEntityTypeKeyMapping(String leftEntityTypeKeyMapping) {
        this.leftEntityTypeKeyMapping = leftEntityTypeKeyMapping;
    }

    public String getRightEntityTypeKey() {
        return rightEntityTypeKey;
    }

    public void setRightEntityTypeKey(String rightEntityTypeKey) {
        this.rightEntityTypeKey = rightEntityTypeKey;
    }

    public String getRightEntityTypeKeyMapping() {
        return rightEntityTypeKeyMapping;
    }

    public void setRightEntityTypeKeyMapping(String rightEntityTypeKeyMapping) {
        this.rightEntityTypeKeyMapping = rightEntityTypeKeyMapping;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }
}
