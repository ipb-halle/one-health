/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import de.ipb_halle.curator.metadata.FieldDefinition;
import de.ipb_halle.curator.metadata.FieldType;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
public abstract class AbstractField <T extends FieldEntity> {
    private final IFieldId id;
    private final FieldDefinition fieldDefinition;

    public AbstractField (IFieldId id, FieldDefinition fieldDefinition) {
        this.id = id;
        this.fieldDefinition = fieldDefinition;
    }

    public IFieldId getId() {
        return this.id;
    }

    public String getFieldName() {
        return this.fieldDefinition.getName();
    }

    public FieldType getFieldType() {
        return this.fieldDefinition.getFieldType();
    }

    public FieldDefinition getFieldDefinition() {
        return this.fieldDefinition;
    }

    public String getTableName() {
        return fieldDefinition.getFieldType().getTableName();
    }

    public boolean isMultivalued() {
        return false;
    }

    public void setElementId(UUID elementId) {
        getId().setElementId(elementId);
    }

    public void setOrder(int order) {
        getId().setOrder(order);
    }

    public abstract Object toCSVcell();
    public abstract T createEntity();
}
