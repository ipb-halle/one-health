/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import de.ipb_halle.curator.fields.integer.DynEnumFieldDTO;
import de.ipb_halle.curator.fields.integer.IntegerFieldDTO;
import de.ipb_halle.curator.fields.text.TextFieldDTO;
import de.ipb_halle.curator.metadata.DynEnum;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import de.ipb_halle.curator.metadata.FieldType;
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
public class FieldConverter {

    @Autowired
    private MetadataRegistry registry;

    public FieldDTO createDTO(FieldEntity entity) {
        IFieldId fieldId = entity.getId();
        FieldDefinitionDTO fieldDefinition = registry.getFieldDefinition(fieldId.getFieldId());
        FieldType fieldType = fieldDefinition.getFieldType();
        switch(fieldType) {
            case INTEGER:
                return IntegerFieldDTO.createDTO(entity, fieldDefinition);
            case ENUM:
                DynEnum dynEnum = registry.getDynEnum(fieldDefinition.getId(),
                        ((FieldEntity<Integer>) entity).getValue());
                return DynEnumFieldDTO.createDTO(entity.getId(), fieldDefinition, dynEnum);
            case TEXT:
                return TextFieldDTO.createDTO(entity, fieldDefinition);
            default:
                throw new UnsupportedOperationException("Not implemented yet.");
        }
    }

    public List<FieldDTO> createDTOs(List<FieldEntity> entities) {
        return entities.stream().map(e -> createDTO(e)).collect(Collectors.toList());
    }
}
