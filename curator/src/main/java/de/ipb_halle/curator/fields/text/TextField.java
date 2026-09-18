/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.text;

import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.fields.AbstractField;
import de.ipb_halle.curator.fields.FieldEntity;
import de.ipb_halle.curator.metadata.FieldDefinition;

/**
 *
 * @author fblocal
 */
public class TextField extends AbstractField<TextFieldEntity> {

    private String value;

    private TextField(IFieldId id, FieldDefinition fieldDefinition, String value) {
        super(id, fieldDefinition);
        this.value = value;
    }

    @Override
    public TextFieldEntity createEntity() {
        return new TextFieldEntity(getId(), value);
    }

    public static TextField createDTO(FieldEntity<String> field, FieldDefinition fieldDefinition) {
        return new TextField(field.getId(), fieldDefinition, field.getValue());
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
