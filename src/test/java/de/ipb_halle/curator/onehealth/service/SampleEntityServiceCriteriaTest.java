package de.ipb_halle.curator.onehealth.service;

import de.ipb_halle.curator.DbTestHelper;
import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.onehealth.dto.SampleEntityDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@Testcontainers
class SampleEntityServiceCriteriaTest {

    static PostgreSQLContainer postgres;

    static {
        try {
            postgres = TestcontainersConfiguration.buildPostgreSQLContainer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public JdbcTemplate jdbcTemplate() {
            return new JdbcTemplate(DataSourceBuilder
                    .create()
                    .url(postgres.getJdbcUrl())
                    .build());
        }
    }

    @Autowired
    private SampleEntityService service;

    @Autowired
    private EntityManager entityManager;

    @DynamicPropertySource
    static void properties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }

    private void clearTable() {
        String sql = "DELETE FROM sample_entity";
        DbTestHelper helper = new DbTestHelper(postgres);
        helper.dbUpdate(sql);
    }

    private void createTestData(UUID id, String name, int value) {
        String sql = "INSERT INTO sample_entity (id, name, value) VALUES (?, ?, ?)";
        DbTestHelper helper = new DbTestHelper(postgres);
        helper.dbUpdate(sql, id, name, value);
    }

    @Test
    void findByCriteria_namePattern() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        // Insert via JDBC (JPQL queries won't see uncommitted data)
        createTestData(id1, "Apple", 5);
        createTestData(id2, "Banana", 10);

        List<SampleEntityDTO> result = service.findByCriteria("ppl", null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Apple");
    }

    @Test
    void findByCriteria_valueRange() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        clearTable();

        // Manually insert data via native SQL
        createTestData(id1, "Low", 5);
        createTestData(id2, "Medium", 50);
        createTestData(id3, "High", 100);

        // Use Criteria API to filter by value range
        List<SampleEntityDTO> result = service.findByCriteria(null, 10, 60);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Medium");
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

        // entityManager.createQuery("DELETE FROM SampleEntity").executeUpdate();
        clearTable();

        createTestData(id1, "X", 42);
        createTestData(id2, "Y", 42);

        List<SampleEntityDTO> result = service.findByValue(42);

        assertThat(result).hasSize(2);
    }
}
