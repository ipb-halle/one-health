package de.ipb_halle.curator.metadata;

/**
 * Immutable descriptor for a {@code element_types} row.
 * Loaded at startup from the database and made available via {@link de.ipb_halle.curator.metadata.MetadataRegistry}.
 */
public final class ElementType {

    public enum ElementClass {
        NODE,
        EDGE;
    }

    private final int id;
    private final ElementClass elementClass;
    private final String label;
    private final String name;
    private final String description;
    private final Integer uiColor;

    public ElementType(int id, ElementClass elementClass, String label, String name, String description, Integer uiColor) {
        this.id = id;
        this.elementClass = elementClass;
        this.label = label;
        this.name = name;
        this.description = description;
        this.uiColor = uiColor;
    }

    public int getId() {
        return id;
    }

    public ElementClass getElementClass() {
        return elementClass;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getUiColor() {
        return uiColor;
    }

    @Override
    public String toString() {
        return "ElementType{id=%d, class=%s, label='%s'}".formatted(id, elementClass.toString(), label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementType that = (ElementType) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name);
    }
}
