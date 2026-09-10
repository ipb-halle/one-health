/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.text;

import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author fblocal
 */
@Component
public class TextFieldConverter {

    @Autowired
    private MetadataRegistry registry;

   public TextField createEntity(TextFieldDTO fieldDTO) {
        return fieldDTO.createEntity();
    }

    public FieldDTO createDTO(TextField field) {
        IFieldId fieldId = field.getId();
        FieldDefinitionDTO fieldDefinition = registry.getFieldDefinition(fieldId.getFieldId());
        return TextFieldDTO.createDTO(field, fieldDefinition);
    }

    public List<FieldDTO> createDTOs(List<TextField> fields) {
        return fields.stream().map(f -> createDTO(f)).collect(Collectors.toList());
    }
}
