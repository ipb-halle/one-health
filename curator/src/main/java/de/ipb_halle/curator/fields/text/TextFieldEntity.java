/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.text;

import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.fields.OrderedFieldId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import de.ipb_halle.curator.fields.FieldEntity;

/**
 *
 * @author fblocal
 */
@Entity
@Table(name="text_fields")
public class TextFieldEntity implements FieldEntity<String> {

    public final static String[] HEADER = { "element_id", "field_id", "field_order", "value"};

    @EmbeddedId
    private OrderedFieldId id;

    @Column
    private String value;

    public TextFieldEntity() {

    }

    public TextFieldEntity(IFieldId id, String value) {
        this.id = new OrderedFieldId(id);
        this.value = value;
    }

    public TextFieldEntity(UUID elementId, int fieldDefinitionId, int order, String value) {
        this.id = new OrderedFieldId(elementId, fieldDefinitionId, order);
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

}
