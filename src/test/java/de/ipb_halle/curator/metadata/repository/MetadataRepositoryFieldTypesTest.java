package de.ipb_halle.curator.metadata.repository;

import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.metadata.enums.FieldTypeEnum;
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
class MetadataRepositoryFieldTypesTest {

    @Autowired
    private MetadataRepository repository;

    @Test
    void findAllFieldTypes_returnsTextEnum() {
        List<FieldTypeEnum> fieldTypes = repository.findAllFieldTypes();

        assertThat(fieldTypes).hasSize(1);
        assertThat(fieldTypes.get(0)).isEqualTo(FieldTypeEnum.TEXT);
    }
}
