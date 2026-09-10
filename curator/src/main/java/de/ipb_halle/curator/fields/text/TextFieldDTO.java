/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.text;

import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;

/**
 *
 * @author fblocal
 */
public class TextFieldDTO extends FieldDTO {

    private String value;

    private TextFieldDTO(IFieldId id, FieldDefinitionDTO fieldDefinition, String value) {
        super(id, fieldDefinition);
        this.value = value;
    }

    public TextField createEntity() {
        return new TextField(getId(), value);
    }

    public static TextFieldDTO createDTO(TextField field, FieldDefinitionDTO fieldDefinition) {
        return new TextFieldDTO(field.getId(), fieldDefinition, field.getValue());
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
