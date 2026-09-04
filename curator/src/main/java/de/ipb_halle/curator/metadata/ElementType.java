package de.ipb_halle.curator.metadata;

import java.util.ArrayList;
import java.util.List;

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
    private final List<FieldDefinitionDTO> fieldDefinitions;

    public ElementType(int id, ElementClass elementClass, String label, String name, String description, Integer uiColor) {
        this.id = id;
        this.elementClass = elementClass;
        this.label = label;
        this.name = name;
        this.description = description;
        this.uiColor = uiColor;
        this.fieldDefinitions = new ArrayList<> ();
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

    public List<FieldDefinitionDTO> getFieldDefinitions() {
        return fieldDefinitions;
    }

    @Override
    public String toString() {
        return "ElementType{id=%d, class=%s, label='%s'}".formatted(id, elementClass.toString(), label);
    }
}
