package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
class MetadataRepositoryTest {

    @Autowired
    private MetadataRepository repository;

    @Test
    void testFindAllElementTypes() {
        List<ElementType> elements = repository.findAllElementTypes();
        assertThat(elements).hasSize(3);
    }

    @Test
    void testFindAllFieldTypes() {
        List<FieldType> fieldTypes = repository.findAllFieldTypes();
        assertThat(fieldTypes).hasSize(1);
    }

    @Test
    void testFindAllFieldDefinitions() {
        List<FieldDefinition> fieldDefinitions = repository.findAllFieldDefinitions();
        assertThat(fieldDefinitions).hasSize(6);
    }
}
