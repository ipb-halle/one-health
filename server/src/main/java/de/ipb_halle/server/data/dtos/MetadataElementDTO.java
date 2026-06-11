package de.ipb_halle.server.data.dtos;

import java.util.List;

public class MetadataElementDTO {
    private String id;
    private String name;
    private Integer count;
    private String description;
    private String type;
    private List<PropertyInfoDTO> properties;

    public MetadataElementDTO() {
    }

    public MetadataElementDTO(String id, String name, Integer count, String description, String type, List<PropertyInfoDTO> properties) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.description = description;
        this.type = type;
        this.properties = properties;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PropertyInfoDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyInfoDTO> properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
