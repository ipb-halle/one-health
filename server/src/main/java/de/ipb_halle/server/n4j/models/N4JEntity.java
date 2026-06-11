package de.ipb_halle.server.n4j.models;

import java.util.List;
import java.util.Map;

public class N4JEntity {
    private String id;
    private List<String> labels;
    private List<Object> properties;

    public N4JEntity(String id, List<String> labels, List<Object> properties){
        this.id = id;
        this.labels = labels;
        this.properties = properties;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getProperties() {
        return properties;
    }

    public void setProperties(List<Object> properties) {
        this.properties = properties;
    }
}
