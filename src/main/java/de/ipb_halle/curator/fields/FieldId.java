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
public class FieldId implements IFieldId {

    @Column(name="element_id")
    private UUID elementId;

    @Column(name="field_id")
    private int fieldId;

    public FieldId() {

    }

    public FieldId(UUID elementId, int fieldId) {
        this.elementId = elementId;
        this.fieldId = fieldId;
    }

    @Override
    public UUID getElementId() {
        return elementId;
    }

    @Override
    public int getFieldId() {
        return fieldId;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public void setOrder(int order) {
        // ignore
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.elementId, this.fieldId);
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
        final FieldId other = (FieldId) obj;
        if (this.fieldId != other.fieldId) {
            return false;
        }
        return Objects.equals(this.elementId, other.elementId);
    }
}
