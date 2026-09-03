/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.fields;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
@Embeddable
public class OrderedFieldId implements IFieldId {

    @Column(name="element_id")
    private final UUID elementId;

    @Column(name="field_id")
    private final int fieldId;

    @Column(name="field_order")
    private int order;

    public OrderedFieldId(UUID elementId, int fieldId, int order) {
        this.elementId = elementId;
        this.fieldId = fieldId;
        this.order = order;
    }

    public UUID getElementId() {
        return elementId;
    }

    public int getFieldId() {
        return fieldId;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.elementId)
                + (37997 * this.fieldId)
                + (91823 * this.order + 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderedFieldId other = (OrderedFieldId) obj;

        if ((this.fieldId != other.fieldId) ||
                (this.order != other.order)) {
            return false;
        }
        return Objects.equals(this.elementId, other.elementId);
    }
}
