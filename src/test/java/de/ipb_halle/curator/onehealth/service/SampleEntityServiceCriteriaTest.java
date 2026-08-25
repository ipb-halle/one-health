package de.ipb_halle.curator.onehealth.service;

import de.ipb_halle.curator.onehealth.dto.SampleEntityDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
// import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest
@Testcontainers
class SampleEntityServiceCriteriaTest {
/*
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("testdb")
        .withUsername("testuser")
        .withPassword("testpass");

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JdbcTemplate jdbcTemplate() {
            return new JdbcTemplate(postgres.getJdbcUrl(), postgres.getDataSource());
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

    @Test
    void findByCriteria_namePattern() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        JdbcTemplate jdbc = new JdbcTemplate(postgres.getJdbcUrl(), postgres.getDataSource());
        TransactionTemplate tt = new TransactionTemplate(entityManager.getTransactionManager());

        // Insert via JDBC (JPQL queries won't see uncommitted data)
        tt.execute(status -> {
            jdbc.update("INSERT INTO sample_entity (id, name, value) VALUES (?, ?, ?)", id1, "Apple", 5);
            jdbc.update("INSERT INTO sample_entity (id, name, value) VALUES (?, ?, ?)", id2, "Banana", 10);
            return null;
        });

        List<SampleEntityDTO> result = service.findByCriteria("app", null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Apple");
    }

    @Test
    void findByCriteria_valueRange() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        JdbcTemplate jdbc = new JdbcTemplate(postgres.getJdbcUrl(), postgres.getDataSource());

        entityManager.createQuery("DELETE FROM SampleEntity").executeUpdate();

        // Manually insert data via native SQL
        try (java.sql.Connection conn = postgres.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO sample_entity (id, name, value) VALUES (?, ?, ?)")) {

            ps.setObject(1, id1); ps.setString(2, "Low", 5); ps.execute();
            ps.setObject(1, id2); ps.setString(2, "Medium", 50); ps.execute();
            ps.setObject(1, id3); ps.setString(2, "High", 95); ps.execute();
        }

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
        JdbcTemplate jdbc = new JdbcTemplate(postgres.getJdbcUrl(), postgres.getDataSource());
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        entityManager.createQuery("DELETE FROM SampleEntity").executeUpdate();

        try (java.sql.Connection conn = postgres.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO sample_entity (id, name, value) VALUES (?, ?, ?)")) {
            ps.setObject(1, id1); ps.setString(2, "X", 42); ps.execute();
            ps.setObject(1, id2); ps.setString(2, "Y", 42); ps.execute();
        }

        List<SampleEntityDTO> result = service.findByValue(42);

        assertThat(result).hasSize(2);
    }
*/
}
