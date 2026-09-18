/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.compound;

import de.ipb_halle.curator.fields.FieldEntity;
import de.ipb_halle.curator.fields.FieldId;
import de.ipb_halle.curator.fields.IFieldId;
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
@Table(name="compound_fields")
public class CompoundField implements FieldEntity<String> {

    public final static String[] HEADER = { "element_id", "field_id", "field_order", "value", "compound"};

    @EmbeddedId
    private FieldId id;

    @Column
    private String value;   // the InChI

    @Column
    private String compound;

    public CompoundField() {

    }

    public CompoundField(UUID elementId, int fieldDefinitionId, int order, String value, String compound) {
        this.id = new FieldId(elementId, fieldDefinitionId);
        this.value = value;
    }

    public IFieldId getId() {
        return this.id;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCompound() {
        return compound;
    }

    public void setCompound(String compound) {
        this.compound = compound;
    }
}
