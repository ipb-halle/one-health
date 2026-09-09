/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.integer;

import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.fields.FieldId;
import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.metadata.DynEnum;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import de.ipb_halle.curator.metadata.FieldType;
import static de.ipb_halle.curator.metadata.FieldType.FieldTypeEnum.ENUM;
import static de.ipb_halle.curator.metadata.FieldType.FieldTypeEnum.INTEGER;
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
public class IntegerFieldConverter {

    @Autowired
    private MetadataRegistry registry;

    public IntegerField createEntity(DynEnumFieldDTO field) {
        return null;
    }

    public IntegerField createEntity(IntegerFieldDTO field) {
        return null;
    }

    public FieldDTO createDTO(IntegerField field) {
        FieldType fieldType = getFieldType(field);
        switch(fieldType.getType()) {
            case INTEGER:
                return IntegerFieldDTO.createDTO(field);

            case ENUM:
                DynEnum dynEnum = getDynEnum(field);
                return DynEnumFieldDTO.createDTO(field.getId(), dynEnum);

            default:
                throw new IllegalArgumentException("Invalid FieldType");
        }

    }

    public List<FieldDTO> createDTOs(List<IntegerField> fields) {
        return fields.stream().map(f -> createDTO(f)).collect(Collectors.toList());
    }

    private FieldType getFieldType(IntegerField field) {
        IFieldId id = field.getId();
        FieldDefinitionDTO fieldDefDTO = registry.getFieldDefinition(id.getFieldId());
        return fieldDefDTO.getFieldType();
    }

    private DynEnum getDynEnum(IntegerField field) {
        IFieldId id = field.getId();
        FieldDefinitionDTO fieldDefDTO = registry.getFieldDefinition(id.getFieldId());
        return registry.getDynEnum(fieldDefDTO.getId(), field.getValue());
    }
}
