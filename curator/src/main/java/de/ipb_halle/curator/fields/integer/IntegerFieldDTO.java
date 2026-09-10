/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.integer;

import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.fields.OrderedFieldId;
import java.util.UUID;
import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;

/**
 *
 * @author fblocal
 */
public class IntegerFieldDTO extends FieldDTO {

    private Integer value;

    private IntegerFieldDTO(IFieldId id, FieldDefinitionDTO fieldDefinition, Integer value) {
        super(id, fieldDefinition);
        this.value = value;
    }

    public static IntegerFieldDTO createDTO(IntegerField field, FieldDefinitionDTO fieldDefinition) {
        return new IntegerFieldDTO(field.getId(), fieldDefinition, field.getValue());
    }

    public IntegerField createEntity() {
        IFieldId id = getId();
        return new IntegerField(id.getElementId(), id.getFieldId(), id.getOrder(), value);
    }


    @Override
    public Object toCSVcell() {
        return value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
