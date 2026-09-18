/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.real;

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
@Table(name="real_fields")
public class RealFieldEntity implements FieldEntity<Double> {

    public final static String[] HEADER = { "element_id", "field_id", "field_order", "value"};

    @EmbeddedId
    private OrderedFieldId id;

    @Column
    private Double value;

    public RealFieldEntity() {

    }

    public RealFieldEntity(UUID elementId, int fieldDefinitionId, int order, Double value) {
        this.id = new OrderedFieldId(elementId, fieldDefinitionId, order);
        this.value = value;
    }

    public IFieldId getId() {
        return this.id;
    }

    @Override
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
