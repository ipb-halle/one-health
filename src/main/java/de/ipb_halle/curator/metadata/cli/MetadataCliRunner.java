package de.ipb_halle.curator.metadata.cli;

import de.ipb_halle.curator.metadata.MetadataRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Phase 1: Verifies metadata availability after application startup.
 * Prints configured node types to stdout for quick visual confirmation.
 * <p>
 * Runs with order 1 — subsequent ETL phases should use @Order(2) and above.
 */
@Component
@Order(1)
public class MetadataCliRunner implements CommandLineRunner {

    @Autowired
    private MetadataRegistry registry;

    @Override
    public void run(String... args) throws Exception {
        if (!registry.isInitialized()) {
            System.err.println("ERROR: Metadata not loaded. Application will proceed without node types or field definitions.");
            return;
        }

        System.out.println("=== Node Types ===");
        registry.getNodeTypesByName().forEach((name, info) ->
                System.out.printf("  [%d] %s (%s) — color=0x%x%n",
                        info.getId(), name, info.getGraphLabel(), info.getUiColor())
        );

        System.out.println();
        System.out.println("=== Field Definitions ===");
        registry.getFieldDefsByName().forEach((name, info) ->
                System.out.printf("  [%d] %s (type=%s, mandatory=%b, multivalued=%b)%n",
                        info.getId(), name, info.getFieldType(),
                        info.isMandatory(), info.isMultivalued())
        );

        System.out.println();
        System.out.println("=== Field Type Groups ===");
        registry.getFieldDefsByType().forEach((type, defs) -> {
            System.out.printf("  %s: %d definition(s)%n", type, defs.size());
        });
    }
}
