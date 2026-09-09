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

/**
 *
 * @author fblocal
 */
public class IntegerFieldDTO implements FieldDTO {

    private OrderedFieldId id;

    private Integer value;

    private IntegerFieldDTO(IFieldId id, Integer value) {
        this.id = new OrderedFieldId(id);
        this.value = value;
    }

    private IntegerFieldDTO(UUID elementId, int fieldDefinitionId, int order, Integer value) {
        this.id = new OrderedFieldId(elementId, fieldDefinitionId, order);
        this.value = value;
    }

    public static IntegerFieldDTO createDTO(IntegerField field) {
        return new IntegerFieldDTO(field.getId(), field.getValue());
    }

    public IntegerField createEntity() {
        return new IntegerField(id.getElementId(), id.getFieldId(), id.getOrder(), value);
    }

    @Override
    public IFieldId getId() {
        return this.id;
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
