package de.ipb_halle.curator.metadata;

/**
 * Immutable descriptor for a {@code node_types} row.
 * Loaded at startup from the database and made available via {@link de.ipb_halle.curator.metadata.MetadataRegistry}.
 */
public final class NodeType {

    private enum ElementType {
        NODE,
        EDGE;
    }

    private final int id;
    private final String name;
    private final String label;
    private final String description;
//    private final ElementType elementType;
    private final Integer uiColor;

    public NodeType(int id, String name, String label, String description, Integer uiColor) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.description = description;
        this.uiColor = uiColor;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Integer getUiColor() {
        return uiColor;
    }

    @Override
    public String toString() {
        return "NodeTypeInfo{id=%d, name='%s'}".formatted(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeType that = (NodeType) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name);
    }
}
