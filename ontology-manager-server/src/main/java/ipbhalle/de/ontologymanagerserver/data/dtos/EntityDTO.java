package ipbhalle.de.ontologymanagerserver.data.dtos;

import java.util.List;

public class EntityDTO {
    private String id;
    private String type;
    private List<String> labels;
    private List<PropertyValueDTO> properties;
    private List<ReferenceDTO> references;
    private List<String> synonyms;
    private String color;

    public EntityDTO() {
    }

    public EntityDTO(String id, String type, List<String> labels, String color){
        this.id = id;
        this.type = type;
        this.labels = labels;
        this.color = color;

    }

    public EntityDTO(List<String> labels, List<PropertyValueDTO> properties) {
        this.labels = labels;
        this.properties = properties;
    }

    public EntityDTO(String id, List<String> labels, List<PropertyValueDTO> properties) {
        this.labels = labels;
        this.properties = properties;
        this.id = id;
    }

    public List<PropertyValueDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyValueDTO> properties) {
        this.properties = properties;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<ReferenceDTO> getReferences() {
        return references;
    }

    public void setReferences(List<ReferenceDTO> references) {
        this.references = references;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }
}