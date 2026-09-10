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
import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;

/**
 *
 * @author fblocal
 */
public class DynEnumFieldDTO extends FieldDTO {

    private DynEnum value;

    private DynEnumFieldDTO(IFieldId fieldId, FieldDefinitionDTO fieldDefinition, DynEnum value) {
        super(fieldId, fieldDefinition);
        this.value = value;
    }

    public static DynEnumFieldDTO createDTO(IFieldId fieldId, FieldDefinitionDTO fieldDefinition, DynEnum value) {
        return new DynEnumFieldDTO(fieldId, fieldDefinition, value);
    }

    public IntegerField getEntity() {
        IFieldId id = getId();
        return new IntegerField(id.getElementId(),
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
