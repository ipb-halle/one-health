package de.ipb_halle.curator.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Phase 0 of the ETL startup pipeline: loads all metadata from the database
 * into the {@link MetadataRegistry}. Runs before any other CLI runner.
 */
@Service
@Order(0)
public class MetadataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MetadataLoader.class);

    @Autowired
    private MetadataRepository repository;

    @Autowired
    private MetadataRegistry registry;


    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading metadata from database...");

        registry.initializeElementTypes(loadElementTypes());
        registry.initializeFieldTypes(loadFieldTypes());
        registry.initializeFieldDefinitions(loadFieldDefinitions());
    }

    private List<ElementType> loadElementTypes() {
        return repository.findAllElementTypes();
    }

    private List<FieldType> loadFieldTypes() {
        return repository.findAllFieldTypes();
    }

    private List<FieldDefinition> loadFieldDefinitions() {
        return repository.findAllFieldDefinitions();
    }
}
