package de.ipb_halle.curator.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Phase 1: dummy workload
 * Prints field definition data to demonstrate workability.
 * Runs with order 1 — subsequent ETL phases should use @Order(2) and above.
 */
@Component
@Order(1)
public class MetadataCliRunner implements CommandLineRunner {

    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private MetadataRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (!registry.isInitialized()) {
            System.err.println("ERROR: Metadata not loaded. Application will proceed without elements or field definitions.");
            return;
        }

        System.out.println();
        System.out.println("=== Field Definitions ===");
        repository.findAllFieldDefinitions().stream()
                .forEach(fieldDef -> {
                    System.out.printf("%s\n", registry.getFieldDefinition(fieldDef.getId()));
                    System.out.printf("    %s\n", registry.getFieldType(fieldDef.getFieldTypeId()));
                    System.out.printf("    %s\n", registry.getElementType(fieldDef.getElementTypeId()));
                    System.out.println();
                });
    }
}
