package de.ipb_halle.server.data.dtos;

public class GraphLinkDTO  {
    private String id;
    private String label;
    private String source;
    private String target;
    private String direction;
    private Integer value;

    public GraphLinkDTO(String id, String label, String source, String target, String direction, Integer value) {
        this.id = id;
        this.label = label;
        this.source = source;
        this.target = target;
        this.direction = direction;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
