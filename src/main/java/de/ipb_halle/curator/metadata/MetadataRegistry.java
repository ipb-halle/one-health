package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.metadata.FieldType.FieldTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;

/**
 * Singleton holder for all immutable metadata maps loaded from the database at startup.
 * Provides read-only access to node types, field type groupings, and field definitions by name.
 */
@Component
public class MetadataRegistry {

    private Map<Integer, NodeType> nodeTypesById;
    private Map<FieldTypeEnum, FieldType> fieldTypesByType;
    private Map<Integer, FieldType> fieldTypesById;
    private Map<Integer, FieldDefinitionDTO> fieldDefinitionsById;
    private Map<String, FieldDefinitionDTO> fieldDefinitionsByKey;
    private boolean nodeTypesInitialized = false;
    private boolean fieldTypesInitialized = false;
    private boolean fieldDefinitionsInitialized = false;

    public void initializeNodeTypes(List<NodeType> nodeTypes) {
        if (nodeTypesInitialized) {
            throw new RuntimeException("Duplicate initialization of NodeTypes");
        }
        nodeTypesById = nodeTypes.stream().collect(toMap(NodeType::getId, Function.identity()));
        nodeTypesInitialized = true;
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
        if (! (nodeTypesInitialized && fieldTypesInitialized)) {
            throw new RuntimeException("Missing initialization of NodeTypes or FieldTypes");
        }
        if (fieldDefinitionsInitialized) {
            throw new RuntimeException("Duplicate initialization of FieldDefinitions");
        }
        List<FieldDefinitionDTO> dtos = fieldDefinitions.stream()
                .map(fieldDef -> new FieldDefinitionDTO(fieldDef,
                        fieldTypesById.get(fieldDef.getFieldTypeId()),
                        nodeTypesById.get(fieldDef.getNodeTypeId())))
                .toList();

        fieldDefinitionsById = dtos.stream().collect(toMap(FieldDefinitionDTO::getId, Function.identity()));
        fieldDefinitionsByKey = dtos.stream().collect(toMap(FieldDefinitionDTO::getKey, Function.identity()));
        fieldDefinitionsInitialized = true;
    }

    public NodeType getNodeType(Integer id) {
        return nodeTypesById.get(id);
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
        return nodeTypesInitialized && fieldTypesInitialized && fieldDefinitionsInitialized;
    }
}
