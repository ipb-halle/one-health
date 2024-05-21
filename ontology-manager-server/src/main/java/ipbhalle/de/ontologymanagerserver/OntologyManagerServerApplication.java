package ipbhalle.de.ontologymanagerserver;

import ipbhalle.de.ontologymanagerserver.services.interfaces.IOntologyInitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class OntologyManagerServerApplication {


	@Autowired
	public OntologyManagerServerApplication(IOntologyInitializerService ontologyInitializerService){
		ontologyInitializerService.EnsureInitialData();
	}


	public static void main(String[] args) {
		SpringApplication.run(OntologyManagerServerApplication.class, args);
	}

}
