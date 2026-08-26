package de.ipb_halle.curator;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.neo4j.Neo4jContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {
    private static final PostgreSQLContainer postgres = buildPostgreSQLContainer();

    @Bean
    @ServiceConnection
    Neo4jContainer neo4jContainer() {
        return new Neo4jContainer(DockerImageName.parse("neo4j:latest"));
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgreSqlContainer() throws Exception {
        return postgres;
    }

    private static PostgreSQLContainer buildPostgreSQLContainer() {
        PostgreSQLContainer container = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("curator")
                .withUsername("curator")
                .withPassword("curator")
                .withCopyToContainer(MountableFile.forClasspathResource("/schema/0001_test_db.sql"), "/docker-entrypoint-initdb.d/0001_test_db.sql");
        container.start();

        return container;
    }
}
