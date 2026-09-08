/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.integer;

import de.ipb_halle.curator.metadata.DynEnum;
import de.ipb_halle.curator.fields.Field;
import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.fields.OrderedFieldId;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
public class DynEnumField implements Field {

    private final OrderedFieldId id;
    private DynEnum value;

    public DynEnumField(UUID elementId, DynEnum value, Integer order) {
            this.id = new OrderedFieldId(elementId, value.getFieldDefinitionId(), order);
            this.value = value;
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

    public DynEnum getValue() {
        return value;
    }

    public void setValue(DynEnum value) {
        this.value = value;
    }

}
