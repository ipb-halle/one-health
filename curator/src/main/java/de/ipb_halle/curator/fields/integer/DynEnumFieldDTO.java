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
import de.ipb_halle.curator.fields.OrderedFieldId;
import de.ipb_halle.curator.fields.FieldDTO;

/**
 *
 * @author fblocal
 */
public class DynEnumFieldDTO implements FieldDTO {

    private final OrderedFieldId id;
    private DynEnum value;

    private DynEnumFieldDTO(IFieldId fieldId, DynEnum value) {
            this.id = new OrderedFieldId(fieldId);
            this.value = value;
    }

    public static DynEnumFieldDTO createDTO(IFieldId fieldId, DynEnum value) {
        return new DynEnumFieldDTO(fieldId, value);
    }

    public IntegerField getEntity() {
        return new IntegerField(id.getElementId(),
                id.getFieldId(),
                id.getOrder(),
                value.getId());
    }

    @Override
    public IFieldId getId() {
        return this.id;
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
