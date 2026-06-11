package de.ipb_halle.server.n4j.repository;


import de.ipb_halle.server.data.interfaces.IEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

@DataNeo4jTest
public class N4JEntityRepositoryTests {

    @Autowired
    private IEntityRepository entityRepository;

}

