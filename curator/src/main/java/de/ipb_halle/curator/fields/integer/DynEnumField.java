/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.integer;

import de.ipb_halle.curator.metadata.DynEnum;
import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.fields.AbstractField;
import de.ipb_halle.curator.metadata.FieldDefinition;

/**
 *
 * @author fblocal
 */
public class DynEnumField extends AbstractField<IntegerFieldEntity> {

    private DynEnum value;

    private DynEnumField(IFieldId fieldId, FieldDefinition fieldDefinition, DynEnum value) {
        super(fieldId, fieldDefinition);
        this.value = value;
    }

    public static DynEnumField createDTO(IFieldId fieldId, FieldDefinition fieldDefinition, DynEnum value) {
        return new DynEnumField(fieldId, fieldDefinition, value);
    }

    @Override
    public IntegerFieldEntity createEntity() {
        IFieldId id = getId();
        return new IntegerFieldEntity(id.getElementId(),
                id.getFieldId(),
                id.getOrder(),
                value.getId());
    }

    @Override
    public Object toCSVcell() {
        return value.getLabel();
    }

    public DynEnum getValue() {
        return value;
    }

    public void setValue(DynEnum value) {
        this.value = value;
    }
}
