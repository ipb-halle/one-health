package de.ipb_halle.curator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.neo4j.Neo4jContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    private static final String SCHEMA_FILE_RESOURCE = "/PostgresqlContainerSchemaFiles";
    private final static String TEST_PASSWORD = "curator";

    private PostgreSQLContainer container;

    @Bean
    @ServiceConnection
    Neo4jContainer neo4jContainer() {
        return new Neo4jContainer(DockerImageName.parse("neo4j:latest"));
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() throws Exception {
        container = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withUsername("postgres")
                .withPassword(TEST_PASSWORD);
        container.setPortBindings(Arrays.asList("65432:5432"));
        container.start();

        for (String schemaFile : getSchemaFiles()) {
            copySchema("schema/" + schemaFile);
            applySchema(schemaFile);
        }
        return container;
    }

    private List<String> getSchemaFiles() {
        List<String> schemaFileNames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        this.getClass().getResourceAsStream(SCHEMA_FILE_RESOURCE)))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    schemaFileNames.add(line /* .strip() */);
                }
            }
        } catch (Exception e) {
            // logger.error("Error configuring schema files for PostgresqlContainerExtension", (Throwable) e);
        }

        return schemaFileNames;
    }

    private void copySchema(String filename) {
        container.copyFileToContainer(MountableFile.forClasspathResource(filename), "/");
    }

    private void applySchema(String filename) throws Exception {
        ExecResult result = container.execInContainer("su", "postgres", "-c psql < /" + filename);
    }

}
