/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.fields.text;

import de.ipb_halle.curator.fields.FieldId;
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
@Table(name="text_fields")
public class TextField {

    public final static String[] HEADER = { "element_id", "field_id", "field_order", "value"};

    @EmbeddedId
    private final OrderedFieldId id;

    @Column
    private String value;

    public TextField(UUID elementId, int fieldDefinitionId, int order, String value) {
        this.id = new OrderedFieldId(elementId, fieldDefinitionId, order);
        this.value = value;
    }

    public IFieldId getId() {
        return this.id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
