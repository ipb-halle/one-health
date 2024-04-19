package ipbhalle.de.ontologymanagerserver.n4j.models;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
public class N4JDataSource {
    @Id @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String name;
    private String path;
    private boolean uploaded;

    public N4JDataSource() {
    }

    public N4JDataSource(String id, String name, String path, boolean uploaded) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.uploaded = uploaded;
    }

    public N4JDataSource(String name, String path, boolean uploaded) {
        this.name = name;
        this.path = path;
        this.uploaded = uploaded;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
}
