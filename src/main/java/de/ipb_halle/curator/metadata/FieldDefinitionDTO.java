package de.ipb_halle.curator.metadata;

/**
 * DTO derived from a {@code field_definitions} row.
 * Loaded at startup from the database and made available via {@link de.ipb_halle.curator.metadata.MetadataRegistry}.
 */
public final class FieldDefinitionDTO {

    private final int id;
    private final FieldType fieldType;
    private final ElementType elementType;
    private final String name;
    private final String description;
    private final boolean mandatory;
    private final boolean multivalued;

    public FieldDefinitionDTO(FieldDefinition fieldDef, FieldType fieldType, ElementType element) {
        this.id = fieldDef.getId();
        this.fieldType = fieldType;
        this.elementType = element;
        this.name = fieldDef.getName();
        this.description = fieldDef.getDescription();
        this.mandatory = fieldDef.isMandatory();
        this.multivalued = fieldDef.isMultivalued();
    }

    public int getId() {
        return id;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public ElementType getElementType() {
        return elementType;
    }

    /**
     * Returns a compound key formed by {@code elementType} and {@code name},
     * separated by a colon (e.g."ORGANISM:primary name").
     */
    public String getKey() {
        return elementType.getLabel() + ":" + name;
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

    @Override
    public String toString() {
        return "FieldDefinitionInfo{id=%d, fieldType='%s' element='%s', name='%s'}"
                .formatted(id, fieldType.getType().toString(), elementType.getLabel(), name.replace("'", "\\'"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDefinitionDTO that = (FieldDefinitionDTO) o;
        return id == that.id && elementType.equals(that.elementType) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, elementType, name);
    }
}
