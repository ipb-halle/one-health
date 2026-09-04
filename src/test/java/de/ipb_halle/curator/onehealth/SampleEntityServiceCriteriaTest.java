package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.DbTestHelper;
import de.ipb_halle.curator.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
class SampleEntityServiceCriteriaTest {

    @Autowired
    private PostgreSQLContainer postgres;

    @Autowired
    private SampleEntityService service;

    private void clearTable(DbTestHelper helper) {
        String sql = "DELETE FROM sample_entity";
        helper.dbUpdate(sql);
    }

    private void createTestData(DbTestHelper helper, UUID id, String name, int value) {
        String sql = "INSERT INTO sample_entity (id, name, value) VALUES (?, ?, ?)";
        helper.dbUpdate(sql, id, name, value);
    }

    @Test
    void findByCriteria_namePattern() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        try (DbTestHelper helper = new DbTestHelper(postgres)) {

            // Insert via JDBC (JPQL queries won't see uncommitted data)
            createTestData(helper, id1, "Apple", 5);
            createTestData(helper, id2, "Banana", 10);

            List<SampleEntityDTO> result = service.findByCriteria("ppl", null, null);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Apple");
        }
    }

    @Test
    void findByCriteria_valueRange() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        try (DbTestHelper helper = new DbTestHelper(postgres)) {
            clearTable(helper);

            // Manually insert data via native SQL
            createTestData(helper, id1, "Low", 5);
            createTestData(helper, id2, "Medium", 50);
            createTestData(helper, id3, "High", 100);

            // Use Criteria API to filter by value range
            List<SampleEntityDTO> result = service.findByCriteria(null, 10, 60);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Medium");
        }
    }

    @Test
    void saveAndFindById() {
        SampleEntityDTO dto = new SampleEntityDTO();
        dto.setName("NewEntity");
        dto.setValue(99);

        SampleEntityDTO saved = service.save(dto);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("NewEntity");
        assertThat(saved.getValue()).isEqualTo(99);

        SampleEntityDTO found = service.findById(saved.getId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("NewEntity");
    }

    @Test
    void findByValue() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        try (DbTestHelper helper = new DbTestHelper(postgres)) {
            // entityManager.createQuery("DELETE FROM SampleEntity").executeUpdate();
            clearTable(helper);

            createTestData(helper, id1, "X", 42);
            createTestData(helper, id2, "Y", 42);

            List<SampleEntityDTO> result = service.findByValue(42);

            assertThat(result).hasSize(2);
        }
    }
}
