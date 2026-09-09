/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator;

import de.ipb_halle.curator.fields.text.TextFieldReader;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.metadata.MetadataRepository;
import de.ipb_halle.curator.onehealth.ElementReader;
import de.ipb_halle.curator.onehealth.NodeWriter;
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
public class NodeWriterCliRunner implements CommandLineRunner {

    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private NodeWriter nodeWriter;

    @Autowired
    private ElementReader elementReader;

    @Autowired
    private TextFieldReader textFieldReader;

    @Override
    public void run(String... args) throws Exception {
        if (!registry.isInitialized()) {
            System.err.println("ERROR: Metadata not loaded. Application will proceed without elements or field definitions.");
            return;
        }
        ElementType elementType = registry.getElementType(1);

        System.out.println();
        System.out.println("=== Field Definitions ===");
        elementType.getFieldDefinitions().stream()
                .forEach(fieldDef -> {
                    System.out.printf("%s\n", registry.getFieldDefinition(fieldDef.getId()));
                    System.out.printf("    %s\n", fieldDef.getElementType().getLabel());
                    System.out.printf("    %s\n", fieldDef.getFieldType().getDescription());
                    System.out.println();
                });
    }
}
