package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.metadata.enums.FieldTypeEnum;
import de.ipb_halle.curator.metadata.model.FieldDefinitionInfo;
import de.ipb_halle.curator.metadata.model.NodeTypeInfo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton holder for all immutable metadata maps loaded from the database at startup.
 * Provides read-only access to node types, field type groupings, and field definitions by name.
 */
@Component
public class MetadataRegistry {

    private Map<String, NodeTypeInfo> nodeTypesByName;
    private Map<FieldTypeEnum, List<FieldDefinitionInfo>> fieldDefsByType;
    private Map<String, FieldDefinitionInfo> fieldDefsByName;

    /**
     * Initialize all maps. Must be called exactly once during application startup.
     */
    public void initialize(Map<String, NodeTypeInfo> nodeTypesByName,
                           Map<FieldTypeEnum, List<FieldDefinitionInfo>> fieldDefsByType,
                           Map<String, FieldDefinitionInfo> fieldDefsByName) {
        this.nodeTypesByName = Collections.unmodifiableMap(new LinkedHashMap<>(nodeTypesByName));
        Map<FieldTypeEnum, List<FieldDefinitionInfo>> copy = new LinkedHashMap<>();
        for (Map.Entry<FieldTypeEnum, List<FieldDefinitionInfo>> entry : fieldDefsByType.entrySet()) {
            copy.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        this.fieldDefsByType = Collections.unmodifiableMap(copy);
        this.fieldDefsByName = Collections.unmodifiableMap(new LinkedHashMap<>(fieldDefsByName));
    }

    /**
     * Get an unmodifiable map of all node types keyed by name (e.g. "Organism", "Compound").
     */
    public Map<String, NodeTypeInfo> getNodeTypesByName() {
        return Collections.unmodifiableMap(nodeTypesByName);
    }

    /**
     * Get a field type grouping: maps each FieldTypeEnum to its list of field definitions.
     * The returned map and its contained lists are unmodifiable.
     */
    public Map<FieldTypeEnum, List<FieldDefinitionInfo>> getFieldDefsByType() {
        return Collections.unmodifiableMap(fieldDefsByType);
    }

    /**
     * Get an unmodifiable map of all field definitions keyed by name (e.g. "title", "description").
     */
    public Map<String, FieldDefinitionInfo> getFieldDefsByName() {
        return Collections.unmodifiableMap(fieldDefsByName);
    }

    /**
     * Check if metadata has been loaded (registry initialized).
     */
    public boolean isInitialized() {
        return nodeTypesByName != null && !nodeTypesByName.isEmpty();
    }
}
