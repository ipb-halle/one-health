package de.ipb_halle.server.n4j.models;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import de.ipb_halle.server.data.enums.DataType;

@Node
public class N4JPropertyInfo {

    @Id @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String name;
    private String description;
    private Boolean key;
    private DataType dataType;
    private int position;

    public N4JPropertyInfo() {
    }

    public N4JPropertyInfo(String id, String name, String description, Boolean key, DataType dataType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.key = key;
        this.dataType = dataType;
    }

    public N4JPropertyInfo(String id, String name, String description, Boolean key, DataType dataType, int position) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.key = key;
        this.dataType = dataType;
        this.position = position;
    }

    public N4JPropertyInfo(String name, String description, Boolean key, DataType dataType) {
        this.name = name;
        this.description = description;
        this.key = key;
        this.dataType = dataType;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
