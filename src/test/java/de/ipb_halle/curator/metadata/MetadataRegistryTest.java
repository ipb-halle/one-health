package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
class MetadataRegistryTest {

    @Autowired
    private MetadataRegistry registry;

    @Test
    void isInitialized_afterStartup() {
        assertThat(registry.isInitialized()).isTrue();
    }

    @Test
    void getElementTypeById_returnsOrganismRecord() {
        ElementType organism = registry.getElementType(1);

        assertThat(organism).isNotNull();
        assertThat(organism.getLabel()).isEqualTo("ORGANISM");
        assertThat(organism.getUiColor().longValue()).isEqualTo(0x297e00);
        assertThat(organism.getFieldDefinitions().size()).isEqualTo(2);
    }
}
