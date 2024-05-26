package ipbhalle.de.ontologymanagerserver.data.dtos;

public class EntitySearchResultDTO {
    private String id;
    private String name;
    private String type;
    private String color;
    private String entityId;

    public EntitySearchResultDTO(){}

    public EntitySearchResultDTO(String id, String name, String type, String color, String entityId){
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
