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
class MetadataRepositoryFieldDefinitionsTest {

    @Autowired
    private MetadataRepository repository;

    @Test
    void findAllFieldDefinitions_returnsTwoEntries() {
        List<FieldDefinitionInfo> fieldDefinitions = repository.findAllFieldDefinitions();

        assertThat(fieldDefinitions).hasSize(2);
    }

    @Test
    void primaryNameFieldDefinition_hasCorrectValues() {
        FieldDefinitionInfo primaryName = repository.findFieldDefinitionById(1).orElseThrow();

        assertThat(primaryName.getId()).isEqualTo(1);
        assertThat(primaryName.getName()).isEqualTo("primary name");
        assertThat(primaryName.getFieldType()).isEqualTo(FieldTypeEnum.TEXT);
        assertThat(primaryName.getDescription())
                .isEqualTo("primary name for a given node type");
        assertThat(primaryName.isMandatory()).isFalse();
        assertThat(primaryName.isMultivalued()).isFalse();
    }

    @Test
    void synonymFieldDefinition_hasCorrectValues() {
        FieldDefinitionInfo synonym = repository.findFieldDefinitionById(2).orElseThrow();

        assertThat(synonym.getId()).isEqualTo(2);
        assertThat(synonym.getName()).isEqualTo("synonym");
        assertThat(synonym.getFieldType()).isEqualTo(FieldTypeEnum.TEXT);
        assertThat(synonym.getDescription())
                .isEqualTo("alternative names for a given node type");
        assertThat(synonym.isMandatory()).isFalse();
        assertThat(synonym.isMultivalued()).isTrue();
    }

    @Test
    void findAllFieldDefinitions_orderedByInsertOrder() {
        List<FieldDefinitionInfo> fieldDefinitions = repository.findAllFieldDefinitions();

        assertThat(fieldDefinitions.get(0).getName()).isEqualTo("primary name");
        assertThat(fieldDefinitions.get(1).getName()).isEqualTo("synonym");
    }
}
