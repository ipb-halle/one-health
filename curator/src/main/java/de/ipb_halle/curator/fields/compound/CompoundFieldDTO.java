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
import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.fields.FieldEntity;
import de.ipb_halle.curator.metadata.FieldDefinition;

/**
 *
 * @author fblocal
 */
public class CompoundFieldDTO extends FieldDTO<CompoundField> {

    private String value;
    private String compound;

    private CompoundFieldDTO(IFieldId id, FieldDefinition fieldDefinition, String value, String  compound) {
        super(id, fieldDefinition);
        this.value = value;
    }

    public static CompoundFieldDTO createDTO(FieldEntity<String> field, FieldDefinition fieldDefinition) {
        return new CompoundFieldDTO(field.getId(), fieldDefinition, field.getValue(), "");
    }

    public CompoundField createEntity() {
        IFieldId id = getId();
        return new CompoundField(id.getElementId(), id.getFieldId(), id.getOrder(), value, compound);
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
