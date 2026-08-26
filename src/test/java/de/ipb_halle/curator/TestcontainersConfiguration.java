package de.ipb_halle.curator;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.neo4j.Neo4jContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    private final static String SCHEMA_DIR = "util/schema/";
    private final static String INIT_DIR = "/docker-entrypoint-initdb.d/";

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
                .withPassword("curator");
        container = addInitDbFile(container, "0001_test_db.sql");
        container.start();
        return container;
    }

    private static PostgreSQLContainer addInitDbFile(PostgreSQLContainer container, String filename) {
        try {
            Path basePath = Paths.get(
                    TestcontainersConfiguration.class.getResource("/").toURI())
                    .getParent().getParent();

            Path file = basePath.resolve(SCHEMA_DIR + filename);

            return container.withCopyToContainer(MountableFile.forHostPath(file), INIT_DIR + filename);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
