package de.ipb_halle.server.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.enums.DataType;
import de.ipb_halle.server.n4j.models.N4JEntityType;
import de.ipb_halle.server.n4j.models.N4JPropertyInfo;
import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Sql(scripts = "/sql/search/seed-search.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SearchControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4j::getAdminPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Autowired
    private Neo4jClient neo4jClient;

    @BeforeEach
    void setUp() {
        neo4jTemplate.deleteAll(N4JEntityType.class);
        neo4jClient.query("MATCH (n:Entity) DETACH DELETE n").run();
        neo4jClient.query("MATCH (n:Synonym) DETACH DELETE n").run();

        N4JPropertyInfo nameProperty = new N4JPropertyInfo("Name", "Display name", true, DataType.STRING);
        nameProperty.setPosition(0);

        N4JPropertyInfo descProperty = new N4JPropertyInfo("Description", "Entity description", false, DataType.STRING);
        descProperty.setPosition(1);

        N4JEntityType diseaseType = new N4JEntityType(
                "Disease",
                "Diseases",
                null,
                "A disease entity type",
                "#FF0000",
                new HashSet<>(),
                new HashSet<>(List.of(descProperty)),
                new HashSet<>(),
                nameProperty
        );

        N4JEntityType savedType = neo4jTemplate.save(diseaseType);

        neo4jClient.query(
                "CREATE (e:Entity {OHUUID: $id, __type: $type, Name: $name, Description: $desc})"
        )
                .bindAll(java.util.Map.of(
                        "id", "entity-aspirin-1",
                        "type", savedType.getId(),
                        "name", "Aspirin",
                        "desc", "A common pain reliever"
                ))
                .run();

        neo4jClient.query(
                "MATCH (e:Entity {OHUUID: $id}) CREATE (e)-[:HAS_SYNONYM]->(:Synonym {name: $synonym})"
        )
                .bindAll(java.util.Map.of(
                        "id", "entity-aspirin-1",
                        "synonym", "Acetylsalicylic acid"
                ))
                .run();
    }

    @Test
    void searchTerm_returnsMatchingEntities() throws Exception {
        var response = mockMvc.perform(get("/api/search").param("query", "aspirin"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<EntityDTO> result = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EntityDTO.class));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("entity-aspirin-1");
        assertThat(result.get(0).getName()).isEqualTo("Aspirin");
        assertThat(result.get(0).getType()).isEqualTo("Disease");
        assertThat(result.get(0).getColor()).isEqualTo("#FF0000");
        assertThat(result.get(0).getSynonyms()).contains("Acetylsalicylic acid");
        assertThat(result.get(0).getProperties()).isNotNull().isNotEmpty();
    }

    @Test
    void searchTerm_returnsEmptyListWhenNoMatch() throws Exception {
        var response = mockMvc.perform(get("/api/search").param("query", "zzz-no-such-term"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<EntityDTO> result = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EntityDTO.class));

        assertThat(result).isEmpty();
    }

    @Test
    void searchTerm_throwsRuntimeExceptionOnSqlInjection() {
        assertThatThrownBy(() ->
                mockMvc.perform(get("/api/search").param("query", "' OR 1=1--"))
        )
        .isInstanceOf(ServletException.class)
        .hasCauseInstanceOf(RuntimeException.class);
    }
}
