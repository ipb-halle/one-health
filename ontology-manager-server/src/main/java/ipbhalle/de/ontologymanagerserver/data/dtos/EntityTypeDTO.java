package ipbhalle.de.ontologymanagerserver.data.dtos;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;

import java.util.Set;

public class EntityTypeDTO extends DTO<String> {
    private String id;
    private String name;
    private String pluralName;
    private EntityTypeDTO parent;
    private String description;
    private String color;
    private Integer objectCount;
    private PropertyInfoDTO label;
    private Set<KeywordDTO> keywords;
    private Set<PropertyInfoDTO> properties;
    private Set<DataSourceDTO> sources;

    public EntityTypeDTO(){}

    public EntityTypeDTO(String id, String name, String pluralName, EntityTypeDTO parent, String description, String color, Set<KeywordDTO> keywords, Set<PropertyInfoDTO> properties,Set<DataSourceDTO> sources) {
        this.id = id;
        this.name = name;
        this.pluralName = pluralName;
        this.parent = parent;
        this.description = description;
        this.color = color;
        this.keywords = keywords;
        this.properties = properties;
        this.sources = sources;
    }

    public EntityTypeDTO(String id, String name, String pluralName, EntityTypeDTO parent, String description, String color, Set<PropertyInfoDTO> properties) {
        this.id = id;
        this.name = name;
        this.pluralName = pluralName;
        this.parent = parent;
        this.description = description;
        this.color = color;
        this.properties = properties;
    }

    public EntityTypeDTO(String name, String pluralName, EntityTypeDTO parent, String description, String color, Set<KeywordDTO> keywords, Set<PropertyInfoDTO> properties, Set<PropertyInfoDTO> inheritedProperties, Set<DataSourceDTO> sources) {
        this.name = name;
        this.pluralName = pluralName;
        this.parent = parent;
        this.description = description;
        this.keywords = keywords;
        this.properties = properties;
        this.sources = sources;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPluralName() {
        return pluralName;
    }

    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    public EntityTypeDTO getParent() {
        return parent;
    }

    public void setParent(EntityTypeDTO parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(Integer objectCount) {
        this.objectCount = objectCount;
    }

    public Set<KeywordDTO> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<KeywordDTO> keywords) {
        this.keywords = keywords;
    }

    public Set<PropertyInfoDTO> getProperties() {
        return properties;
    }

    public void setProperties(Set<PropertyInfoDTO> properties) {
        this.properties = properties;
    }

    public Set<DataSourceDTO> getSources() {
        return sources;
    }

    public void setSources(Set<DataSourceDTO> sources) {
        this.sources = sources;
    }

    public PropertyInfoDTO getLabel() {
        return label;
    }

    public void setLabel(PropertyInfoDTO label) {
        this.label = label;
    }
}
