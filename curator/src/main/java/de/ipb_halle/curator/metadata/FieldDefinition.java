/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

/**
 * DTO derived from a {@code field_definitions} row.
 * Loaded at startup from the database and made available via {@link de.ipb_halle.curator.metadata.MetadataRegistry}.
 */
public final class FieldDefinition {

    private final int id;
    private final FieldType fieldType;
    private final ElementType elementType;
    private final Integer graphExportOrder;
    private final String name;
    private final String description;
    private final boolean mandatory;
    private final boolean multivalued;

    public FieldDefinition(FieldDefinitionEntity fdEntity, ElementType element) {
        this.id = fdEntity.getId();
        this.graphExportOrder = fdEntity.getGraphExportOrder();
        this.fieldType = fdEntity.getFieldType();
        this.elementType = element;
        this.name = fdEntity.getName();
        this.description = fdEntity.getDescription();
        this.mandatory = fdEntity.isMandatory();
        this.multivalued = fdEntity.isMultivalued();
    }

    public int getId() {
        return id;
    }

    public Integer getGraphExportOrder() {
        return graphExportOrder;
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
        return elementType.getId() + ":" + name;
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
