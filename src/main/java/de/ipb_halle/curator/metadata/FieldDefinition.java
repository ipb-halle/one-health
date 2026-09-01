package de.ipb_halle.curator.metadata;

/**
 * Immutable descriptor for a {@code field_definitions} row.
 * Loaded at startup from the database and made available via {@link de.ipb_halle.curator.metadata.MetadataRegistry}.
 */
public final class FieldDefinition {

    private final int id;
    private final int fieldTypeId;
    private final int nodeTypeId;
    private final String name;
    private final String description;
    private final boolean mandatory;
    private final boolean multivalued;

    public FieldDefinition(int id, int fieldTypeId, int nodeTypeId, String name,
                               String description, boolean mandatory, boolean multivalued) {
        this.id = id;
        this.fieldTypeId = fieldTypeId;
        this.nodeTypeId = nodeTypeId;
        this.name = name;
        this.description = description;
        this.mandatory = mandatory;
        this.multivalued = multivalued;
    }

    public int getId() {
        return id;
    }

    public int getFieldTypeId() {
        return fieldTypeId;
    }

    public int getNodeTypeId() {
        return nodeTypeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isMultivalued() {
        return multivalued;
    }
}
