package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
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
class MetadataRepositoryNodeTypesTest {

    @Autowired
    private MetadataRepository repository;

    @BeforeEach
    void setup() {
        // Ensure tables are empty — seed data loaded via docker-entrypoint only once
    }

    @Test
    void findAllNodeTypes_returnsThreeEntries() {
        List<NodeTypeInfo> nodeTypes = repository.findAllNodeTypes();

        assertThat(nodeTypes).hasSize(3);
    }

    @Test
    void organimsNodeType_hasCorrectValues() {
        NodeTypeInfo organism = repository.findNodeTypeById(1).orElseThrow();

        assertThat(organism.getName()).isEqualTo("Organism");
        assertThat(organism.getGraphLabel()).isEqualTo("ORGANISM");
        assertThat(organism.getDescription()).isEqualTo("Living cellular organism");
        assertThat(organism.getUiColor()).isEqualTo(0x297e00);
    }

    @Test
    void compoundNodeType_hasCorrectValues() {
        NodeTypeInfo compound = repository.findNodeTypeById(2).orElseThrow();

        assertThat(compound.getName()).isEqualTo("Compound");
        assertThat(compound.getGraphLabel()).isEqualTo("COMPOUND");
        assertThat(compound.getDescription())
                .isEqualTo("Chemical compound, ideally produced by a living organism and thus a natural product");
    }

    @Test
    void diseaseNodeType_hasCorrectValues() {
        NodeTypeInfo disease = repository.findNodeTypeById(3).orElseThrow();

        assertThat(disease.getName()).isEqualTo("Disease");
        assertThat(disease.getGraphLabel()).isEqualTo("DISEASE");
        assertThat(disease.getDescription())
                .isEqualTo("A condition that impairs the normal functioning of the body or one of its parts, and it is typically associated with specific symptoms and signs.");
    }

    @Test
    void findAllNodeTypes_orderedByInsertOrder() {
        List<NodeTypeInfo> nodeTypes = repository.findAllNodeTypes();

        assertThat(nodeTypes.get(0).getName()).isEqualTo("Organism");
        assertThat(nodeTypes.get(1).getName()).isEqualTo("Compound");
        assertThat(nodeTypes.get(2).getName()).isEqualTo("Disease");
    }
}
