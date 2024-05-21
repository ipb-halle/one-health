package ipbhalle.de.ontologymanagerserver.n4j.repository;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IOntologyRepository;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JEntityType;
import org.neo4j.driver.QueryConfig;
import org.springframework.data.neo4j.core.Neo4jOperations;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class N4JOntologyRepository implements IOntologyRepository {

    private Neo4jOperations neo4jOperations;

    public N4JOntologyRepository(Neo4jOperations neo4jOperations) {
        this.neo4jOperations = neo4jOperations;
    }

    @Override
    public List<EntityDTO> GetAsGraph() {
        String query = "match (n: Entity) return { labels: labels(n), properties: properties(n) }";
        var a = neo4jOperations.findAll(query, EntityDTO.class);
        return a;
    }

    @Override
    public GraphDTO FindCoOccurrences(CoOcurrenceQuery query) {

        StringBuilder result = new StringBuilder();

        result.append("MATCH ");

        query.getLeftTypeQuery().setFilters( query.getLeftTypeQuery().getFilters().stream().filter(x -> !x.getValue().isEmpty()).toList());
        query.getRightTypeQuery().setFilters( query.getRightTypeQuery().getFilters().stream().filter(x -> !x.getValue().isEmpty()).toList());


        var leftEntityType = neo4jOperations.findById(query.getLeftTypeQuery().getType(), N4JEntityType.class).get();
        var rightEntityType = neo4jOperations.findById(query.getRightTypeQuery().getType(), N4JEntityType.class).get();

        // match the type
        result.append("(x:`").append(leftEntityType.getName()).append("`");

        if (query.getLeftTypeQuery().getFilters() != null && !query.getLeftTypeQuery().getFilters().isEmpty()){
            var leftEntityCriteria = query.getLeftTypeQuery().getFilters().stream().map(
                    x -> String.format("`%s`: '%s'", x.getProperty(), x.getValue())).toList();
            result.append(" { ").append(String.join(",", leftEntityCriteria)).append(" }");
        }

        result.append(")");


        // add the relationship
        result.append("- [r] - ");


        result.append("(y:`").append(rightEntityType.getName()).append("`");
        if (query.getRightTypeQuery().getFilters() != null && !query.getRightTypeQuery().getFilters().isEmpty()){
            var rightEntityCriteria = query.getRightTypeQuery().getFilters().stream().map(
                    x -> String.format("`%s`: '%s'", x.getProperty(), x.getValue())).toList();
            result.append(" { ").append(String.join(",", rightEntityCriteria)).append(" }");
        }
        result.append(")");

        result.append("WITH type(r) AS label, count(r) AS value, ");

        var leftNodeIdentifier = query.getLeftTypeQuery().getGroupBy() != null ?
                String.format("x.`%s`", query.getLeftTypeQuery().getGroupBy()) :
                String.format("'%s'", query.getLeftTypeQuery().getType());

        var rightNodeIdentifier = query.getRightTypeQuery().getGroupBy() != null ?
                String.format("y.`%s`", query.getRightTypeQuery().getGroupBy()) :
                String.format("'%s'", query.getRightTypeQuery().getType());

        result.append(leftNodeIdentifier).append(" AS source, ");
        result.append(rightNodeIdentifier).append(" AS target ");


        result.append("RETURN source, target, label, value");


        var links = neo4jOperations.findAll(result.toString(), GraphLinkDTO.class);

        var sourceNodeLabels = new HashSet<>(links.stream().map(GraphLinkDTO::getSource).toList());
        var targetNodeLabels = new HashSet<>(links.stream().map(GraphLinkDTO::getTarget).toList());

        var nodes = Stream.concat(
                sourceNodeLabels.stream().map(x -> new GraphNodeDTO(x, leftEntityType.getColor())),
                targetNodeLabels.stream().map(x -> new GraphNodeDTO(x, rightEntityType.getColor()))
        ).toList();

        var graph = new GraphDTO(nodes, links);

        return graph;


    }


    @Override
    public GraphDTO GetInitialSet() {
        var query = "match(n:Disease {Name: 'Malaria'}) return n";

        var node = neo4jOperations.findAll(query, GraphNodeDTO.class);

        return new GraphDTO(node, new ArrayList<GraphLinkDTO>());
    }

    @Override
    public GraphDTO GetAdjacentNodes(String nodeId, List<String> nodes) {
        return null;
    }

    @Override
    public EntityDTO GetNode(String nodeId) {
        return null;
    }

    @Override
    public GraphLinkDTO GetLink(String linkId) {
        return null;
    }

    @Override
    public List<LinkDTO> GetLinks(String sourceId, String targetId, String type) {
        return List.of();
    }
}
