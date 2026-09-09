/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

/**
 * Immutable descriptor for a {@code field_definitions} row.
 * Loaded at startup from the database and made available via {@link de.ipb_halle.curator.metadata.MetadataRegistry}.
 */
public final class FieldDefinition {

    private final int id;
    private final int fieldTypeId;
    private final int elementTypeId;
    private final Integer graphExportOrder;
    private final String name;
    private final String description;
    private final boolean mandatory;
    private final boolean multivalued;

    public FieldDefinition(int id, int fieldTypeId, int elementTypeId, Integer graphExportOrder, String name,
                               String description, boolean mandatory, boolean multivalued) {
        this.id = id;
        this.fieldTypeId = fieldTypeId;
        this.elementTypeId = elementTypeId;
        this.graphExportOrder = graphExportOrder;
        this.name = name;
        this.description = description;
        this.mandatory = mandatory;
        this.multivalued = multivalued;
    }

    public int getId() {
        return id;
    }

    public int getFieldTypeId() {
        return fieldTypeId;
    }

    public int getElementTypeId() {
        return elementTypeId;
    }

    public Integer getGraphExportOrder() {
        return graphExportOrder;
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
