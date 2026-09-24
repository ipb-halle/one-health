/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import de.ipb_halle.curator.fields.compound.CompoundFieldEntity;
import de.ipb_halle.curator.fields.compound.CompoundField;
import de.ipb_halle.curator.fields.integer.DynEnumField;
import de.ipb_halle.curator.fields.integer.IntegerFieldEntity;
import de.ipb_halle.curator.fields.integer.IntegerField;
import de.ipb_halle.curator.fields.real.RealFieldEntity;
import de.ipb_halle.curator.fields.text.TextFieldEntity;
import de.ipb_halle.curator.fields.text.TextField;
import de.ipb_halle.curator.metadata.DynEnum;
import de.ipb_halle.curator.metadata.FieldDefinition;
import de.ipb_halle.curator.metadata.FieldType;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.util.List;
import java.util.UUID;
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

    public AbstractField fromString(UUID elementId, FieldDefinition fieldDefinition, String value) {
        FieldType fieldType = fieldDefinition.getFieldType();
        switch(fieldType) {
            case INTEGER:
                return createDTO(new IntegerFieldEntity(
                        elementId,
                        fieldDefinition.getId(),
                        0,
                        Integer.valueOf(value)));
            case ENUM:
                DynEnum dynEnum = registry.getDynEnum(fieldDefinition.getId(), value);
                if (dynEnum == null) {
                    throw new IllegalArgumentException("Undefined DynEnum value: " + value);
                }
                return createDTO(new IntegerFieldEntity(
                        elementId,
                        fieldDefinition.getId(),
                        0,
                        dynEnum.getId()));
            case TEXT:
                return createDTO(new TextFieldEntity(
                        elementId,
                        fieldDefinition.getId(),
                        0,
                        value));
            case REAL:
                return createDTO(new RealFieldEntity(
                        elementId,
                        fieldDefinition.getId(),
                        0,
                        Double.valueOf(value)));
            case COMPOUND:
                return createDTO(new CompoundFieldEntity(
                        elementId,
                        fieldDefinition.getId(),
                        0,
                        value));
            default:
                throw new UnsupportedOperationException("Not implemented yet.");
        }
    }

    public AbstractField createDTO(FieldEntity entity) {
        IFieldId fieldId = entity.getId();
        FieldDefinition fieldDefinition = registry.getFieldDefinition(fieldId.getFieldId());
        FieldType fieldType = fieldDefinition.getFieldType();
        switch(fieldType) {
            case INTEGER:
                return IntegerField.createDTO(entity, fieldDefinition);
            case ENUM:
                DynEnum dynEnum = registry.getDynEnum(fieldDefinition.getId(),
                        ((FieldEntity<Integer>) entity).getValue());
                if (dynEnum == null) {
                    throw new IllegalArgumentException("Unknown DynEnum for fieldId '%d'"
                            .formatted(fieldDefinition.getId()));
                }
                return DynEnumField.createDTO(entity.getId(), fieldDefinition, dynEnum);
            case TEXT:
                return TextField.createDTO(entity, fieldDefinition);
            case COMPOUND:
                return CompoundField.createDTO(entity, fieldDefinition);
            default:
                throw new UnsupportedOperationException("Not implemented yet.");
        }
    }

    public List<AbstractField> createDTOs(List<FieldEntity> entities) {
        return entities.stream().map(e -> createDTO(e)).collect(Collectors.toList());
    }
}
