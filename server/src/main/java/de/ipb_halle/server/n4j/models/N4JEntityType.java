package de.ipb_halle.server.n4j.models;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Set;

@Node
public class N4JEntityType {

    @Id @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String name;
    private String pluralName;

    @Relationship(type = "PARENT_OF", direction = Relationship.Direction.INCOMING)
    private N4JEntityType parent;

    private String description;
    private String color;


    @Relationship(type = "HAS_KEYWORD", direction = Relationship.Direction.OUTGOING)
    private Set<N4JKeyword> keywords;

    @Relationship(type = "HAS_PROPERTY", direction = Relationship.Direction.OUTGOING)
    private Set<N4JPropertyInfo> properties;

    @Relationship(type = "INHERITS_PROPERTY", direction = Relationship.Direction.OUTGOING)
    private Set<N4JPropertyInfo> inheritedProperties;

    @Relationship(type = "FROM_DATASOURCE", direction = Relationship.Direction.OUTGOING)
    private Set<N4JDataSource> sources;

    @Relationship(type = "__HAS_LABEL", direction = Relationship.Direction.OUTGOING)
    private N4JPropertyInfo label;

    public N4JEntityType() {};

    public N4JEntityType(String id) {
        this.id = id;
    }



    public N4JEntityType(String id, String name, String pluralName, N4JEntityType parent, String description, String color, Set<N4JKeyword> keywords, Set<N4JPropertyInfo> properties, Set<N4JPropertyInfo> inheritedProperties, Set<N4JDataSource> sources) {
        this.id = id;
        this.name = name;
        this.pluralName = pluralName;
        this.parent = parent;
        this.description = description;
        this.keywords = keywords;
        this.properties = properties;
        this.sources = sources;
        this.inheritedProperties = inheritedProperties;
        this.color = color;
    }

    public N4JEntityType(String name, String pluralName, N4JEntityType parent, String description, String color, Set<N4JKeyword> keywords, Set<N4JPropertyInfo> properties, Set<N4JPropertyInfo> inheritedProperties, Set<N4JDataSource> sources) {
        this.name = name;
        this.pluralName = pluralName;
        this.parent = parent;
        this.description = description;
        this.keywords = keywords;
        this.properties = properties;
        this.sources = sources;
        this.inheritedProperties = inheritedProperties;
        this.color = color;
    }

    public N4JEntityType(String name, String pluralName, N4JEntityType parent, String description, String color, Set<N4JKeyword> keywords, Set<N4JPropertyInfo> properties, Set<N4JPropertyInfo> inheritedProperties, Set<N4JDataSource> sources, N4JPropertyInfo label) {
        this.name = name;
        this.pluralName = pluralName;
        this.parent = parent;
        this.description = description;
        this.keywords = keywords;
        this.properties = properties;
        this.sources = sources;
        this.inheritedProperties = inheritedProperties;
        this.color = color;
        this.label = label;
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

    public String getPluralName() {
        return pluralName;
    }

    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    public N4JEntityType getParent() {
        return parent;
    }

    public void setParent(N4JEntityType parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public Set<N4JPropertyInfo> getInheritedProperties() {
        return inheritedProperties;
    }

    public void setInheritedProperties(Set<N4JPropertyInfo> inheritedProperties) {
        this.inheritedProperties = inheritedProperties;
    }

    public N4JPropertyInfo getLabel() {
        return label;
    }

    public void setLabel(N4JPropertyInfo label) {
        this.label = label;
    }
}
