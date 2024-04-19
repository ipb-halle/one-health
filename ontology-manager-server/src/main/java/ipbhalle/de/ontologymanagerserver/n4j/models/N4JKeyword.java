package ipbhalle.de.ontologymanagerserver.n4j.models;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
public class N4JKeyword {

    @Id @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String value;

    public N4JKeyword() {
    }

    public N4JKeyword(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public N4JKeyword(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
