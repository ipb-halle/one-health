package de.ipb_halle.server.data.dtos;

import java.util.Set;

import de.ipb_halle.server.data.enums.Cardinality;
import de.ipb_halle.server.data.interfaces.DTO;

public class LinkTypeDTO extends DTO<String> {
    private String id;
    private String name;
    private String direction;
    private String leftEntityTypeId;
    private Cardinality leftCardinality;
    private String rightEntityTypeId;
    private Cardinality rightCardinality;
    private String description;
    private Set<KeywordDTO> keywords;
    private Set<PropertyInfoDTO> properties;
    private Set<DataSourceDTO> sources;

    public LinkTypeDTO() {
    }

    public LinkTypeDTO(String id, String name, String direction, String leftEntityTypeId, Cardinality leftCardinality, String rightEntityTypeId, Cardinality rightCardinality, String description, Set<KeywordDTO> keywords, Set<PropertyInfoDTO> properties, Set<DataSourceDTO> sources) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.leftEntityTypeId = leftEntityTypeId;
        this.leftCardinality = leftCardinality;
        this.rightEntityTypeId = rightEntityTypeId;
        this.rightCardinality = rightCardinality;
        this.description = description;
        this.keywords = keywords;
        this.properties = properties;
        this.sources = sources;
    }

    public LinkTypeDTO(String name, String direction, String leftEntityTypeId, Cardinality leftCardinality, String rightEntityTypeId, Cardinality rightCardinality, String description, Set<KeywordDTO> keywords, Set<PropertyInfoDTO> properties, Set<DataSourceDTO> sources) {
        this.name = name;
        this.direction = direction;
        this.leftEntityTypeId = leftEntityTypeId;
        this.leftCardinality = leftCardinality;
        this.rightEntityTypeId = rightEntityTypeId;
        this.rightCardinality = rightCardinality;
        this.description = description;
        this.keywords = keywords;
        this.properties = properties;
        this.sources = sources;
    }

    public LinkTypeDTO(String id,String name, String direction, String leftEntityTypeId, Cardinality leftCardinality, String rightEntityTypeId, Cardinality rightCardinality, String description) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.leftEntityTypeId = leftEntityTypeId;
        this.leftCardinality = leftCardinality;
        this.rightEntityTypeId = rightEntityTypeId;
        this.rightCardinality = rightCardinality;
        this.description = description;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLeftEntityTypeId() {
        return leftEntityTypeId;
    }

    public void setLeftEntityTypeId(String leftEntityTypeId) {
        this.leftEntityTypeId = leftEntityTypeId;
    }

    public Cardinality getLeftCardinality() {
        return leftCardinality;
    }

    public void setLeftCardinality(Cardinality leftCardinality) {
        this.leftCardinality = leftCardinality;
    }

    public String getRightEntityTypeId() {
        return rightEntityTypeId;
    }

    public void setRightEntityTypeId(String rightEntityTypeId) {
        this.rightEntityTypeId = rightEntityTypeId;
    }

    public Cardinality getRightCardinality() {
        return rightCardinality;
    }

    public void setRightCardinality(Cardinality rightCardinality) {
        this.rightCardinality = rightCardinality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
