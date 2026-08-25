package de.ipb_halle.curator.onehealth.dto;

import java.util.UUID;

/**
 * Data transfer object for SampleEntity.
 */
public class SampleEntityDTO {

    private UUID id;
    private String name;
    private int value;

    public SampleEntityDTO() {
    }

    public SampleEntityDTO(UUID id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

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
