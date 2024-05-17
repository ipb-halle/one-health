package ipbhalle.de.ontologymanagerserver.n4j.repository;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphLinkDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphNodeDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IGraphRepository;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class N4JEntityRepository implements IEntityRepository {

    private final Neo4jTemplate neo4jTemplate;

    N4JEntityRepository(Neo4jTemplate neo4jTemplate){
        this.neo4jTemplate = neo4jTemplate;
    }


    @Override
    public GraphDTO GetInitialSet() {
        var query = "match(n:Disease {Name: 'Malaria'}) return toString(id(n)) as id, 'Malaria' as label,'#0000FF' as color, 1 as count ";

        var node = neo4jTemplate.findAll(query, GraphNodeDTO.class);

        return new GraphDTO(node, new ArrayList<GraphLinkDTO>());
    }

    @Override
    public GraphDTO GetAdjacentNodes(String nodeId) {

        String result = "match(n) - [r] - (m:Entity) where id(n) = " +
                nodeId +
                " return toString(id(n)) as source, toString(id(m)) as target, type(r) as label, 1 as value;";
        var links = neo4jTemplate.findAll(result, GraphLinkDTO.class);


        String result2 = "match(n) - [r] - (m:Entity) where id(n) = " +
                nodeId +
                " return toString(id(m)) as id, toString(id(m)) as label, '#0000FF' as color, 1 as count";
        var nodes = neo4jTemplate.findAll(result2, GraphNodeDTO.class);

        return new GraphDTO(nodes, links);
    }

    @Override
    public GraphNodeDTO GetNode(String nodeId) {
        return null;
    }

    @Override
    public GraphLinkDTO GetLink(String linkId) {
        return null;
    }
}
