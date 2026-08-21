package de.ipb_halle.onehealth;

import java.util.UUID;

public class SampleEntity {
    private UUID id;
    private String name;
    private int value;


    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    
}
