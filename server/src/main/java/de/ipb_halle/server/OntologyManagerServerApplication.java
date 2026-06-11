package de.ipb_halle.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import de.ipb_halle.server.services.interfaces.IOntologyInitializerService;

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
