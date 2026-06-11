package de.ipb_halle.server.data.dtos;

import java.util.HashMap;

public class EntityTypeMappingDTO {

    private String type;
    private HashMap<String, PropertyInfoDTO> properties;

    public EntityTypeMappingDTO() {
    }

    public EntityTypeMappingDTO(String type, HashMap<String, PropertyInfoDTO> properties) {
        this.type = type;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, PropertyInfoDTO> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, PropertyInfoDTO> properties) {
        this.properties = properties;
    }
}
