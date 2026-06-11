package de.ipb_halle.server.data.dtos;

public class SynonymDTO
{
    private String name;
    private String entityId;

    public SynonymDTO() {}
    public SynonymDTO(String name, String entityId) {

        this.name = name;
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityId() {
        return entityId;
    }
}
