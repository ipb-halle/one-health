package ipbhalle.de.ontologymanagerserver.data.dtos;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Map;
import java.util.List;
import java.util.Set;

public class EntityDTO extends DTO<String> {
    private List<String> labels;

    private Map<String, String> properties;

    public EntityDTO() {
    }

    public EntityDTO(List<String> labels, Map<String, String> properties) {
        this.labels = labels;
        this.properties = properties;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}