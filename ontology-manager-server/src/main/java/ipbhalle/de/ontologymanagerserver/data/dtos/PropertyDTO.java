package ipbhalle.de.ontologymanagerserver.data.dtos;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;

import java.util.Set;

public class PropertyDTO extends DTO<String> {
    private String id;
    private PropertyInfoDTO info;
    private Set<PropertyValueDTO> values;

    public PropertyDTO(String id, PropertyInfoDTO info, Set<PropertyValueDTO> values) {
        this.id = id;
        this.info = info;
        this.values = values;
    }

    public PropertyDTO(PropertyInfoDTO info, Set<PropertyValueDTO> values) {
        this.info = info;
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PropertyInfoDTO getInfo() {
        return info;
    }

    public void setInfo(PropertyInfoDTO info) {
        this.info = info;
    }

    public Set<PropertyValueDTO> getValues() {
        return values;
    }

    public void setValues(Set<PropertyValueDTO> values) {
        this.values = values;
    }
}
