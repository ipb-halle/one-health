package de.ipb_halle.curator.metadata;

/**
 * Represents the available field types from the {@code field_types} table.
 * This enum is populated from the database on startup and should not be modified at runtime.
 */
public enum FieldTypeEnum {
    TEXT;

    /**
     * Creates a FieldTypeEnum from its uppercase name string.
     *
     * @param name the uppercase field type name from the database
     * @return the corresponding FieldTypeEnum value
     * @throws IllegalArgumentException if the name is null, empty, or does not match any enum constant
     */
    public static FieldTypeEnum fromName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Field type name must not be null or blank");
        }
        return valueOf(name.trim().toUpperCase());
    }
}
