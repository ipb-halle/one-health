/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

import java.util.Collections;
import java.util.HashMap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import static java.util.stream.Collectors.toMap;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Singleton holder for all immutable metadata maps loaded from the database at startup.
 * Provides read-only access to element_types, field type groupings, and field definitions by name.
 * Order of initialization matters.
 */
@Component
public class MetadataRegistry {

    @Autowired
    private MetadataRepository repository;

    private Map<Integer, Map<String, DynEnum>> dynEnumsByLabel;
    private Map<Integer, Map<Integer, DynEnum>> dynEnumsById;
    private Map<String, ElementType> elementTypesById;
    private Map<Integer, FieldDefinitionDTO> fieldDefinitionsById;
    private Map<String, FieldDefinitionDTO> fieldDefinitionsByKey;
    private boolean dynEnumsInitialized = false;
    private boolean elementTypesInitialized = false;
    private boolean fieldDefinitionsInitialized = false;

    public void initializeDynEnums(List<DynEnum> dynEnums) {
        if (! fieldDefinitionsInitialized) {
            throw new RuntimeException("Missing initialization of FieldDefinitions");
        }
        if (dynEnumsInitialized) {
            throw new RuntimeException("Duplicate initialization of DynEnums");
        }
        dynEnumsByLabel = new HashMap<> ();
        dynEnumsById = new HashMap<> ();
        for (DynEnum d : dynEnums) {
            mapDynEnum(d);
        }
        dynEnumsInitialized = true;
    }

    private void mapDynEnum(DynEnum d) {
        Integer fieldId = d.getFieldDefinitionId();
        Map<String, DynEnum> byLabel = dynEnumsByLabel.getOrDefault(
                fieldId, new HashMap<> ());
        byLabel.put(d.getLabel(), d);
        dynEnumsByLabel.put(fieldId, byLabel);
        Map<Integer, DynEnum> byId = dynEnumsById.getOrDefault(
                fieldId, new HashMap<> ());
        byId.put(d.getId(), d);
        dynEnumsById.put(fieldId, byId);
    }

    public void initializeElementTypes(List<ElementType> elementTypes) {
        if (elementTypesInitialized) {
            throw new RuntimeException("Duplicate initialization of ElementTypes");
        }
        elementTypesById = elementTypes.stream().collect(toMap(ElementType::getId, Function.identity()));
        elementTypesInitialized = true;
    }

    public void initializeFieldDefinitions(List<FieldDefinition> fieldDefinitions) {
        if (! (elementTypesInitialized)) {
            throw new RuntimeException("Missing initialization of ElementTypes");
        }
        if (fieldDefinitionsInitialized) {
            throw new RuntimeException("Duplicate initialization of FieldDefinitions");
        }
        List<FieldDefinitionDTO> dtos = fieldDefinitions.stream()
                .map(fieldDef -> registerFieldDefinition(fieldDef))
                .toList();

        fieldDefinitionsById = dtos.stream().collect(toMap(FieldDefinitionDTO::getId, Function.identity()));
        fieldDefinitionsByKey = dtos.stream().collect(toMap(FieldDefinitionDTO::getKey, Function.identity()));
        fieldDefinitionsInitialized = true;
    }

    private FieldDefinitionDTO registerFieldDefinition(FieldDefinition fieldDef) {
        ElementType elementType = getElementType(fieldDef.getElementTypeId());
        FieldDefinitionDTO dto = new FieldDefinitionDTO(fieldDef,
                        elementType);
        elementType.getFieldDefinitions().add(dto);
        return dto;
    }

    public DynEnum getDynEnum(Integer fieldId, String label) {
        if (dynEnumsByLabel.containsKey(fieldId)) {
            return dynEnumsByLabel.get(fieldId).get(label);
        }
        throw new IllegalArgumentException("Field '%d' is not a DynEnum field".formatted(fieldId));
    }

    public DynEnum getDynEnum(Integer fieldId, Integer id) {
        if (dynEnumsById.containsKey(fieldId)) {
            return dynEnumsById.get(fieldId).get(id);
        }
        throw new IllegalArgumentException("Field '%d' is not a DynEnum field".formatted(fieldId));
    }

    public Map<String, DynEnum> getDynEnumsByFieldId(Integer fieldId) {
        if (dynEnumsById.containsKey(fieldId)) {
            return Collections.unmodifiableMap (dynEnumsByLabel.get(fieldId));
        }
        throw new IllegalArgumentException("DynEnum not defined for fieldId '%d'".formatted(fieldId));
    }

    public DynEnum registerDynEnum(DynEnum dynEnum) {
        if (fieldDefinitionsById.get(dynEnum.getFieldDefinitionId()).getFieldType() != FieldType.ENUM) {
            throw new IllegalArgumentException("Can't register DynEnum for non-DynEnum field '%d'"
                    .formatted(dynEnum.getFieldDefinitionId()));
        }
        synchronized (this) {
            try {
                DynEnum saved = repository.save(dynEnum);
                mapDynEnum(saved);
                return saved;
            } catch(Exception e) {
                throw new IllegalArgumentException("Registration of DynEnum failed.", e);
            }
        }
    }


    public ElementType getElementType(String id) {
        return elementTypesById.get(id);
    }

    public FieldDefinitionDTO getFieldDefinition(Integer id) {
        return fieldDefinitionsById.get(id);
    }

    public FieldDefinitionDTO getFieldDefinition(String key) {
        return fieldDefinitionsByKey.get(key);
    }

    public boolean isInitialized() {
        return elementTypesInitialized && fieldDefinitionsInitialized;
    }
}
