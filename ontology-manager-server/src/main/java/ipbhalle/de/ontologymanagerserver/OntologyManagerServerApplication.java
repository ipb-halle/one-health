package ipbhalle.de.ontologymanagerserver;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class OntologyManagerServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OntologyManagerServerApplication.class, args);
	}

}
