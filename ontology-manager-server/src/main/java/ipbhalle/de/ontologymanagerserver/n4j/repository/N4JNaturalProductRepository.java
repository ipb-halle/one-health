package ipbhalle.de.ontologymanagerserver.n4j.repository;

import ipbhalle.de.ontologymanagerserver.data.dtos.NaturalProductDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.INaturalProductRepository;
import ipbhalle.de.ontologymanagerserver.data.query.NaturalProductStructureQuery;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class N4JNaturalProductRepository implements INaturalProductRepository {

    private final Neo4jTemplate neo4jTemplate;

    N4JNaturalProductRepository(Neo4jTemplate neo4jTemplate){
        this.neo4jTemplate = neo4jTemplate;
    }


    @Override
    public NaturalProductDTO Get(String id) {
        return null;
    }

    @Override
    public List<NaturalProductDTO> GetByStructure(NaturalProductStructureQuery query) {
        return List.of();
    }


    @Override
    public List<NaturalProductDTO> GetByProperties() {
        return List.of();
    }
}
