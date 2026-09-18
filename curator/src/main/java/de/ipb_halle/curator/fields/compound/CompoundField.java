/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.compound;

import de.ipb_halle.curator.fields.integer.*;
import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.fields.AbstractField;
import de.ipb_halle.curator.fields.FieldEntity;
import de.ipb_halle.curator.metadata.FieldDefinition;

/**
 *
 * @author fblocal
 */
public class CompoundField extends AbstractField<CompoundFieldEntity> {

    private String value;
    private String compound;

    private CompoundField(IFieldId id, FieldDefinition fieldDefinition, String value, String  compound) {
        super(id, fieldDefinition);
        this.value = value;
    }

    public static CompoundField createDTO(FieldEntity<String> field, FieldDefinition fieldDefinition) {
        return new CompoundField(field.getId(), fieldDefinition, field.getValue(), "");
    }

    public CompoundFieldEntity createEntity() {
        IFieldId id = getId();
        return new CompoundFieldEntity(id.getElementId(), id.getFieldId(), id.getOrder(), value, compound);
    }


    @Override
    public Object toCSVcell() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setCompound(String compound) {
        this.compound = compound;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
