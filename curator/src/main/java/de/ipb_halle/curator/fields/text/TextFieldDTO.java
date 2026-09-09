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
import de.ipb_halle.curator.fields.FieldDTO;

/**
 *
 * @author fblocal
 */
public class TextFieldDTO implements FieldDTO {

    private final OrderedFieldId id;

    private String value;

    private TextFieldDTO(IFieldId id, String value) {
        this.id = new OrderedFieldId(id);
        this.value = value;
    }

    public TextField createEntity() {
        return new TextField(id, value);
    }

    public static TextFieldDTO createDTO(TextField field) {
        return new TextFieldDTO(field.getId(), field.getValue());
    }

    @Override
    public IFieldId getId() {
        return this.id;
    }

    @Override
    public Object toCSVcell() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
