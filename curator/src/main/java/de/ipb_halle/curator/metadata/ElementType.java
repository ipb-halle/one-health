/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
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

    private final String id;
    private final ElementClass elementClass;
    private final String name;
    private final String description;
    private final Integer uiColor;
    private final List<FieldDefinitionDTO> fieldDefinitions;

    public ElementType(String id, ElementClass elementClass, String name, String description, Integer uiColor) {
        this.id = id;
        this.elementClass = elementClass;
        this.name = name;
        this.description = description;
        this.uiColor = uiColor;
        this.fieldDefinitions = new ArrayList<> ();
    }

    public String getId() {
        return id;
    }

    public ElementClass getElementClass() {
        return elementClass;
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
}
