/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.integer;

import de.ipb_halle.curator.fields.FieldEntity;
import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.fields.OrderedFieldId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
@Entity
@Table(name="integer_fields")
public class IntegerFieldEntity implements FieldEntity<Integer> {

    public final static String[] HEADER = { "element_id", "field_id", "field_order", "value"};

    @EmbeddedId
    private OrderedFieldId id;

    @Column
    private Integer value;

    public IntegerFieldEntity() {

    }

    public IntegerFieldEntity(UUID elementId, int fieldDefinitionId, int order, Integer value) {
        this.id = new OrderedFieldId(elementId, fieldDefinitionId, order);
        this.value = value;
    }

    public IFieldId getId() {
        return this.id;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
