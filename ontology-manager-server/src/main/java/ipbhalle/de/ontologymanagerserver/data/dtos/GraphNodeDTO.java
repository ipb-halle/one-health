package ipbhalle.de.ontologymanagerserver.data.dtos;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;

public class GraphNodeDTO {
    private String id;
    private String label;
    private String color;
    private Integer count;

    public GraphNodeDTO() {
    }

    public GraphNodeDTO(String label) {
        this.label = label;
    }

    public GraphNodeDTO(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public GraphNodeDTO(String id, String label, String color, Integer count) {
        this.id = id;
        this.label = label;
        this.color = color;
        this.count = count;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
