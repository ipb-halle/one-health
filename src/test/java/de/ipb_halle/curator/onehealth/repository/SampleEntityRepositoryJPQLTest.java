package de.ipb_halle.curator.onehealth.repository;

import de.ipb_halle.curator.DbTestHelper;
import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.onehealth.SampleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.DataSourceBuilder;

@DataJpaTest
@Testcontainers
class SampleEntityRepositoryJPQLTest {

    static PostgreSQLContainer postgres;

    static {
        try {
            postgres = TestcontainersConfiguration.buildPostgreSQLContainer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /*
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
     */
    @Autowired
    private SampleEntityRepository repository;

    @DynamicPropertySource
    static void properties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    private void createTestData(UUID id, String name, int value) {
        String sql = "INSERT INTO sample_entity (id, name, value) VALUES (?, ?, ?)";
        DbTestHelper helper = new DbTestHelper(postgres);
        helper.dbUpdate(sql, id, name, value);
    }

    @Test
    void saveAndFindByUUID_JPQL() {
        // insert directly into testcontainer database to bypass Hibernate
        UUID testId = UUID.randomUUID();
        createTestData(testId, "TestEntity", 42);

        Optional<SampleEntity> result = repository.findByIdJPQL(testId);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("TestEntity");
        assertThat(result.get().getValue()).isEqualTo(42);
    }

    @Test
    void findByValue_JPQL() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        createTestData(id1, "Alpha", 10);
        createTestData(id2, "Beta", 10);
        createTestData(id3, "Gamma", 20);

        List<SampleEntity> result = repository.findByValueJPQL(10);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(SampleEntity::getName)
                .containsExactly("Alpha", "Beta");
    }
}
