package de.ipb_halle.curator.metadata;

/**
 * Represents the available field types from the {@code field_types} table.
 */
public class FieldType {

    public enum FieldTypeEnum {
        TEXT,
        INTEGER,
        ENUM,
        UUID;
    }

    private final int id;
    private final FieldTypeEnum type;
    private final String description;
    private final String tableName;

    public FieldType(int id, FieldTypeEnum type, String description, String tableName) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.tableName = tableName;
    }

    public int getId() {
        return id;
    }

    public FieldTypeEnum getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return String.format("FieldType{id=%d, %s}",
                id,
                type.toString());
    }
}
