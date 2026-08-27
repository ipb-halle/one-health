package de.ipb_halle.curator.metadata.service;

import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.metadata.enums.FieldTypeEnum;
import de.ipb_halle.curator.metadata.model.FieldDefinitionInfo;
import de.ipb_halle.curator.metadata.model.NodeTypeInfo;
import de.ipb_halle.curator.metadata.repository.MetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Phase 0 of the ETL startup pipeline: loads all metadata from the database
 * into the {@link MetadataRegistry}. Runs before any other CLI runner.
 */
@Service
@Order(0)
public class MetadataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MetadataLoader.class);

    private final MetadataRepository metadataRepository;
    private final MetadataRegistry registry;

    public MetadataLoader(MetadataRepository metadataRepository, MetadataRegistry registry) {
        this.metadataRepository = metadataRepository;
        this.registry = registry;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading metadata from database...");

        Map<String, NodeTypeInfo> nodeTypesByName = loadNodeTypesByName();
        List<FieldDefinitionInfo> allFieldDefs = loadAllFieldDefinitions();
        Map<FieldTypeEnum, List<FieldDefinitionInfo>> fieldDefsByType = groupFieldDefsByType(allFieldDefs);
        Map<String, FieldDefinitionInfo> fieldDefsByName = mapFieldDefsByName(allFieldDefs);

        if (nodeTypesByName.isEmpty() || fieldDefsByName.isEmpty()) {
            logger.error("Metadata loading failed: node_types or field_definitions table is empty. Application will not function correctly.");
            throw new IllegalStateException("Critical metadata tables are empty — application cannot proceed without node types and field definitions");
        }

        registry.initialize(nodeTypesByName, fieldDefsByType, fieldDefsByName);

        logger.info("Metadata loaded: {} node types, {} field definitions",
                nodeTypesByName.size(), fieldDefsByName.size());
    }

    private Map<String, NodeTypeInfo> loadNodeTypesByName() {
        List<NodeTypeInfo> nodeTypes = metadataRepository.findAllNodeTypes();
        return nodeTypes.stream()
                .collect(Collectors.toMap(
                        NodeTypeInfo::getName,
                        nt -> nt,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    private List<FieldDefinitionInfo> loadAllFieldDefinitions() {
        return metadataRepository.findAllFieldDefinitions();
    }

    private Map<FieldTypeEnum, List<FieldDefinitionInfo>> groupFieldDefsByType(List<FieldDefinitionInfo> fieldDefs) {
        return fieldDefs.stream()
                .collect(Collectors.groupingBy(
                        FieldDefinitionInfo::getFieldType,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    private Map<String, FieldDefinitionInfo> mapFieldDefsByName(List<FieldDefinitionInfo> fieldDefs) {
        return fieldDefs.stream()
                .collect(Collectors.toMap(
                        FieldDefinitionInfo::getName,
                        fd -> fd,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }
}
