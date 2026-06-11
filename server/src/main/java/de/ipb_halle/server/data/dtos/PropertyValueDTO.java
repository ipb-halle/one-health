package de.ipb_halle.server.data.dtos;

import de.ipb_halle.server.data.enums.DataType;

public class PropertyValueDTO {
    private String name;
    private Object value;
    private DataType dataType;
    private int position;

    public PropertyValueDTO() {}

    public PropertyValueDTO(String name, Object value, DataType dataType, int position){
        this.name = name;
        this.value = value;
        this.dataType = dataType;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
