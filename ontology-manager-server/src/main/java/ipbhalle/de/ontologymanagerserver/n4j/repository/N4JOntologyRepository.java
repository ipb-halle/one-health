package ipbhalle.de.ontologymanagerserver.n4j.repository;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IOntologyRepository;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JEntityType;
import org.neo4j.cypherdsl.core.*;

import org.springframework.data.neo4j.core.Neo4jOperations;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class N4JOntologyRepository implements IOntologyRepository {

    private final Neo4jOperations neo4jOperations;

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

        // TODO: Move this input sanitation to service layer :)
        query.getLeftTypeQuery().setFilters( query.getLeftTypeQuery().getFilters().stream().filter(x -> x.getValue() != null && !x.getValue().isEmpty()).toList());
        query.getRightTypeQuery().setFilters( query.getRightTypeQuery().getFilters().stream().filter(x -> x.getValue() != null && !x.getValue().isEmpty()).toList());

        var leftEntityType = neo4jOperations.findById(query.getLeftTypeQuery().getType(), N4JEntityType.class).get();
        var rightEntityType = neo4jOperations.findById(query.getRightTypeQuery().getType(), N4JEntityType.class).get();

        Node nodeX = Cypher.node(leftEntityType.getName()).named("x");
        Node nodeY = Cypher.node(rightEntityType.getName()).named("y");
        Relationship coOccurrenceRelationship = nodeX.relationshipBetween(nodeY).named("r");

        var conditions = Conditions.isTrue();

        for (var filter: query.getLeftTypeQuery().getFilters()){
            conditions = conditions.and(
                    nodeX.property(filter.getProperty()).matches("(?i).*" + filter.getValue() + ".*")
            );
        }

        for (var filter: query.getRightTypeQuery().getFilters()){
            conditions = conditions.and(
                    nodeY.property(filter.getProperty()).matches("(?i).*" + filter.getValue() + ".*")
            );
        }

        var getCoOccurrencesQuery = Cypher
                .match(coOccurrenceRelationship)
                .where(conditions)
                .returning(
                        Functions.type(coOccurrenceRelationship).as("label"),
                        Functions.count(coOccurrenceRelationship.asExpression()).as("value"),
                        nodeX.property(query.getLeftTypeQuery().getGroupBy().isEmpty() ? leftEntityType.getLabel().getName() : query.getLeftTypeQuery().getGroupBy()).as("source"),
                        nodeY.property(query.getRightTypeQuery().getGroupBy().isEmpty() ? rightEntityType.getLabel().getName() : query.getRightTypeQuery().getGroupBy()).as("target")
                ).build();



        var links = neo4jOperations.findAll(getCoOccurrencesQuery, GraphLinkDTO.class);

        var sourceNodeLabels = new HashSet<>(links.stream().map(GraphLinkDTO::getSource).toList());
        var targetNodeLabels = new HashSet<>(links.stream().map(GraphLinkDTO::getTarget).toList());

        var nodes = Stream.concat(
                sourceNodeLabels.stream().map(x -> new GraphNodeDTO(x, leftEntityType.getColor())),
                targetNodeLabels.stream().map(x -> new GraphNodeDTO(x, rightEntityType.getColor()))
        ).toList();

        var graph = new GraphDTO(nodes, links);

        return graph;


    }

    public List<LinkDTO> FindCoOccurrencesDetails(CoOcurrenceQuery query) {
        query.getLeftTypeQuery().setFilters( query.getLeftTypeQuery().getFilters().stream().filter(x -> x.getValue() != null && !x.getValue().isEmpty()).toList());
        query.getRightTypeQuery().setFilters( query.getRightTypeQuery().getFilters().stream().filter(x -> x.getValue() != null && !x.getValue().isEmpty()).toList());


        var leftEntityType = neo4jOperations.findById(query.getLeftTypeQuery().getType(), N4JEntityType.class).get();
        var rightEntityType = neo4jOperations.findById(query.getRightTypeQuery().getType(), N4JEntityType.class).get();

        Node nodeX = Cypher.node(leftEntityType.getName()).named("x");
        Node nodeY = Cypher.node(rightEntityType.getName()).named("y");
        Relationship coOccurrenceRelationship = nodeX.relationshipBetween(nodeY).named("r");


        var conditions = Conditions.isTrue();

        for (var filter: query.getLeftTypeQuery().getFilters()){
            conditions = conditions.and(
                    nodeX.property(filter.getProperty()).matches("(?i).*" + filter.getValue() + ".*")
            );
        }

        for (var filter: query.getRightTypeQuery().getFilters()){
            conditions = conditions.and(
                    nodeY.property(filter.getProperty()).matches("(?i).*" + filter.getValue() + ".*")
            );
        }

        var getCoOccurrencesDetailsQuery = Cypher
                .match(coOccurrenceRelationship)
                .where(conditions)
                .returning(
                        nodeX.property(leftEntityType.getLabel().getName()).as("leftEntity"),
                        nodeY.property(rightEntityType.getLabel().getName()).as("rightEntity"),
                        Functions.type(coOccurrenceRelationship).as("type"),
                        coOccurrenceRelationship.property("source").as("sourceName"),
                        coOccurrenceRelationship.property("sourceurl").as("sourceUrl")

                ).build();


        var links = neo4jOperations.findAll(getCoOccurrencesDetailsQuery, LinkDTO.class);

        return links;
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
