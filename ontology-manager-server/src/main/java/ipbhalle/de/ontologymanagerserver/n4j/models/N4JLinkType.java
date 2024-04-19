package ipbhalle.de.ontologymanagerserver.n4j.models;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.enums.Cardinality;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Set;

@Node
public class N4JLinkType {
    @Id @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String name;
    private String direction;
    private N4JEntityType leftEntityType;
    private Cardinality leftCardinality;
    private N4JEntityType rightEntityType;
    private Cardinality rightCardinality;
    private String description;

    @Relationship(type = "HAS_KEYWORD", direction = Relationship.Direction.OUTGOING)
    private Set<N4JKeyword> keywords;

    @Relationship(type = "HAS_PROPERTY", direction = Relationship.Direction.OUTGOING)
    private Set<N4JPropertyInfo> properties;

    @Relationship(type = "FROM_DATASOURCE", direction = Relationship.Direction.OUTGOING)
    private Set<N4JDataSource> sources;

    public N4JLinkType() {
    }

    public N4JLinkType(String id) {
        this.id = id;
    }

    public N4JLinkType(String id, String name, String direction, N4JEntityType leftEntityType, Cardinality leftCardinality, N4JEntityType rightEntityType, Cardinality rightCardinality, String description, Set<N4JKeyword> keywords, Set<N4JPropertyInfo> properties, Set<N4JDataSource> sources) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.leftEntityType = leftEntityType;
        this.leftCardinality = leftCardinality;
        this.rightEntityType = rightEntityType;
        this.rightCardinality = rightCardinality;
        this.description = description;
        this.keywords = keywords;
        this.properties = properties;
        this.sources = sources;
    }

    public N4JLinkType(String name, String direction, N4JEntityType leftEntityType, Cardinality leftCardinality, N4JEntityType rightEntityType, Cardinality rightCardinality, String description, Set<N4JKeyword> keywords, Set<N4JPropertyInfo> properties, Set<N4JDataSource> sources) {
        this.name = name;
        this.direction = direction;
        this.leftEntityType = leftEntityType;
        this.leftCardinality = leftCardinality;
        this.rightEntityType = rightEntityType;
        this.rightCardinality = rightCardinality;
        this.description = description;
        this.keywords = keywords;
        this.properties = properties;
        this.sources = sources;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public N4JEntityType getLeftEntityType() {
        return leftEntityType;
    }

    public void setLeftEntityType(N4JEntityType leftEntityType) {
        this.leftEntityType = leftEntityType;
    }

    public Cardinality getLeftCardinality() {
        return leftCardinality;
    }

    public void setLeftCardinality(Cardinality leftCardinality) {
        this.leftCardinality = leftCardinality;
    }

    public N4JEntityType getRightEntityType() {
        return rightEntityType;
    }

    public void setRightEntityType(N4JEntityType rightEntityType) {
        this.rightEntityType = rightEntityType;
    }

    public Cardinality getRightCardinality() {
        return rightCardinality;
    }

    public void setRightCardinality(Cardinality rightCardinality) {
        this.rightCardinality = rightCardinality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<N4JKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<N4JKeyword> keywords) {
        this.keywords = keywords;
    }

    public Set<N4JPropertyInfo> getProperties() {
        return properties;
    }

    public void setProperties(Set<N4JPropertyInfo> properties) {
        this.properties = properties;
    }

    public Set<N4JDataSource> getSources() {
        return sources;
    }

    public void setSources(Set<N4JDataSource> sources) {
        this.sources = sources;
    }
}
