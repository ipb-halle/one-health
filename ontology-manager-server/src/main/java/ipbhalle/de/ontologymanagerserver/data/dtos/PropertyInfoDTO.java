package ipbhalle.de.ontologymanagerserver.data.dtos;

import ipbhalle.de.ontologymanagerserver.data.enums.DataType;
import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;

public class PropertyInfoDTO extends DTO<String> {
    private String id;
    private String name;
    private String description;
    private Boolean key;
    private DataType dataType;
    private Boolean inherited;
    private int position;
    private boolean label;

    public PropertyInfoDTO() {};

    public PropertyInfoDTO(String id, String name, String description, Boolean key, DataType dataType, Boolean inherited) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.key = key;
        this.dataType = dataType;
        this.inherited = inherited;
    }

    public PropertyInfoDTO(String id, String name, String description, Boolean key, DataType dataType, Boolean inherited, int position) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.key = key;
        this.dataType = dataType;
        this.inherited = inherited;
        this.position = position;
    }



    public PropertyInfoDTO(String id, String name, String description, Boolean key, DataType dataType, Boolean inherited, int position, boolean label) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.key = key;
        this.dataType = dataType;
        this.inherited = inherited;
        this.position = position;
        this.label = label;
    }


    public PropertyInfoDTO(String name, String description, Boolean key, DataType dataType, Boolean inherited) {
        this.name = name;
        this.description = description;
        this.key = key;
        this.dataType = dataType;
        this.inherited = inherited;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getKey() {
        return key;
    }

    public void setKey(Boolean key) {
        this.key = key;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Boolean getInherited() {
        return inherited;
    }

    public void setInherited(Boolean inherited) {
        this.inherited = inherited;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isLabel() {
        return label;
    }

    public void setLabel(boolean label) {
        this.label = label;
    }
}
