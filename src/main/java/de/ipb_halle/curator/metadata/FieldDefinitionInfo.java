package de.ipb_halle.curator.metadata;

/**
 * Immutable descriptor for a {@code field_definitions} row.
 * Loaded at startup from the database and made available via {@link de.ipb_halle.curator.metadata.MetadataRegistry}.
 */
public final class FieldDefinitionInfo {

    private final int id;
    private final String name;
    private final FieldTypeEnum fieldType;
    private final String description;
    private final boolean mandatory;
    private final boolean multivalued;

    public FieldDefinitionInfo(int id, String name, FieldTypeEnum fieldType,
                               String description, boolean mandatory, boolean multivalued) {
        this.id = id;
        this.name = name;
        this.fieldType = fieldType;
        this.description = description;
        this.mandatory = mandatory;
        this.multivalued = multivalued;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FieldTypeEnum getFieldType() {
        return fieldType;
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

    @Override
    public String toString() {
        return "FieldDefinitionInfo{id=%d, name='%s', type=%s}".formatted(id, name.replace("'", "\\'"), fieldType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDefinitionInfo that = (FieldDefinitionInfo) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name);
    }
}
