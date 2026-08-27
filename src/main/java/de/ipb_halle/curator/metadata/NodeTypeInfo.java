package de.ipb_halle.curator.metadata;

/**
 * Immutable descriptor for a {@code node_types} row.
 * Loaded at startup from the database and made available via {@link de.ipb_halle.curator.metadata.MetadataRegistry}.
 */
public final class NodeTypeInfo {

    private final int id;
    private final String name;
    private final String graphLabel;
    private final String description;
    private final Integer uiColor;

    public NodeTypeInfo(int id, String name, String graphLabel, String description, Integer uiColor) {
        this.id = id;
        this.name = name;
        this.graphLabel = graphLabel;
        this.description = description;
        this.uiColor = uiColor;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGraphLabel() {
        return graphLabel;
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
        NodeTypeInfo that = (NodeTypeInfo) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name);
    }
}
