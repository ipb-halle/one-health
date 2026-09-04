package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.metadata.FieldType.FieldTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import static java.util.stream.Collectors.toMap;

/**
 * Singleton holder for all immutable metadata maps loaded from the database at startup.
 * Provides read-only access to element_types, field type groupings, and field definitions by name.
 * Order of initialization matters.
 */
@Component
public class MetadataRegistry {

    private Map<Integer, ElementType> elementTypesById;
    private Map<FieldTypeEnum, FieldType> fieldTypesByType;
    private Map<Integer, FieldType> fieldTypesById;
    private Map<Integer, FieldDefinitionDTO> fieldDefinitionsById;
    private Map<String, FieldDefinitionDTO> fieldDefinitionsByKey;
    private boolean elementTypesInitialized = false;
    private boolean fieldTypesInitialized = false;
    private boolean fieldDefinitionsInitialized = false;

    public void initializeElementTypes(List<ElementType> elementTypes) {
        if (elementTypesInitialized) {
            throw new RuntimeException("Duplicate initialization of ElementTypes");
        }
        elementTypesById = elementTypes.stream().collect(toMap(ElementType::getId, Function.identity()));
        elementTypesInitialized = true;
    }

    public void  initializeFieldTypes(List<FieldType> fieldTypes) {
        if (fieldTypesInitialized) {
            throw new RuntimeException("Duplicate initialization of FieldTypes");
        }
        fieldTypesById = fieldTypes.stream().collect(toMap(FieldType::getId, Function.identity()));
        fieldTypesByType = fieldTypes.stream().collect(toMap(FieldType::getType, Function.identity()));
        fieldTypesInitialized = true;
    }

    public void initializeFieldDefinitions(List<FieldDefinition> fieldDefinitions) {
        if (! (elementTypesInitialized && fieldTypesInitialized)) {
            throw new RuntimeException("Missing initialization of ElementTypes or FieldTypes");
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
                        getFieldType(fieldDef.getFieldTypeId()),
                        elementType);
        elementType.getFieldDefinitions().add(dto);
        return dto;
    }

    public ElementType getElementType(Integer id) {
        return elementTypesById.get(id);
    }

    public FieldType getFieldType(Integer id) {
        return fieldTypesById.get(id);
    }

    public FieldType getFieldType(FieldTypeEnum type) {
        return fieldTypesByType.get(type);
    }

    public FieldDefinitionDTO getFieldDefinition(Integer id) {
        return fieldDefinitionsById.get(id);
    }

    public FieldDefinitionDTO getFieldDefinition(String key) {
        return fieldDefinitionsByKey.get(key);
    }

    public boolean isInitialized() {
        return elementTypesInitialized && fieldTypesInitialized && fieldDefinitionsInitialized;
    }
}
