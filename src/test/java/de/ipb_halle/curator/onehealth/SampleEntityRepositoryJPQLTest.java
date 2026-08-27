package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.DbTestHelper;
import de.ipb_halle.curator.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
class SampleEntityRepositoryJPQLTest {

    @Autowired
    private PostgreSQLContainer postgres;

    @Autowired
    private SampleEntityRepository repository;

/*
    This is static and would not have access to the 'postgres' field.
    The PostgreSQLContainer is created dynamically for each test (we
    changed it from singleton pattern to per test dynamic creation.
    I think: therefore we cannot obtain an instance in the static
    context and Spring has to auto-wire everything by itself.

    Keep this until we have more ...Repository...Tests as references
    for AI agents. Otherwise they might suggest to introduce DynamicPropertySources
    over and over.

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        PostgreSQLContainer pg = TestcontainersConfiguration.getPostgreSQLContainer();
        registry.add("spring.datasource.url", pg::getJdbcUrl);
        registry.add("jakarta.persistence.jdbc.url", pg::getJdbcUrl);
        registry.add("hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.datasource.username", () -> pg.getUsername());
        registry.add("spring.datasource.password", () -> pg.getPassword());
    }
*/
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
