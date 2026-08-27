package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

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
    void getNodeTypesByName_returnsOrganismRecord() {
        Map<String, NodeTypeInfo> nodeTypes = registry.getNodeTypesByName();

        NodeTypeInfo organism = nodeTypes.get("Organism");
        assertThat(organism).isNotNull();
        assertThat(organism.getGraphLabel()).isEqualTo("ORGANISM");
        assertThat(organism.getUiColor()).isEqualTo(0x297e00);
    }

    @Test
    void getNodeTypesByName_returnsCompoundRecord() {
        Map<String, NodeTypeInfo> nodeTypes = registry.getNodeTypesByName();

        NodeTypeInfo compound = nodeTypes.get("Compound");
        assertThat(compound).isNotNull();
        assertThat(compound.getName()).isEqualTo("Compound");
        assertThat(compound.getDescription())
                .contains("Chemical compound");
    }

    @Test
    void getFieldDefsByType_returnsTextFields() {
        Map<FieldTypeEnum, List<FieldDefinitionInfo>> fieldDefsByType = registry.getFieldDefsByType();

        List<FieldDefinitionInfo> textFields = fieldDefsByType.get(FieldTypeEnum.TEXT);
        assertThat(textFields).isNotNull();
        assertThat(textFields).hasSize(2);
    }

    @Test
    void getFieldDefsByName_returnsPrimaryNameRecord() {
        Map<String, FieldDefinitionInfo> fieldDefs = registry.getFieldDefsByName();

        FieldDefinitionInfo primaryName = fieldDefs.get("primary name");
        assertThat(primaryName).isNotNull();
        assertThat(primaryName.isMandatory()).isFalse();
        assertThat(primaryName.getDescription())
                .isEqualTo("primary name for a given node type");
    }

    @Test
    void getFieldDefsByName_returnsSynonymRecord() {
        Map<String, FieldDefinitionInfo> fieldDefs = registry.getFieldDefsByName();

        FieldDefinitionInfo synonym = fieldDefs.get("synonym");
        assertThat(synonym).isNotNull();
        assertThat(synonym.isMultivalued()).isTrue();
        assertThat(synonym.getFieldType()).isEqualTo(FieldTypeEnum.TEXT);
    }
}
