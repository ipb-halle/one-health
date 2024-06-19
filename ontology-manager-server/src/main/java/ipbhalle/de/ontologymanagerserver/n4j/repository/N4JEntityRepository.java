package ipbhalle.de.ontologymanagerserver.n4j.repository;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IGraphRepository;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JEntity;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JEntityType;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JPropertyInfo;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JPropertyValue;
import org.neo4j.cypherdsl.core.*;
import org.neo4j.driver.internal.value.MapValue;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public GraphDTO GetAdjacentNodes(String nodeId, List<String> nodesIds) {

        HashMap<String, String> colors = new HashMap<>();
        HashMap<String, String> labels = new HashMap<>();

        var nodeTypes = neo4jTemplate.findAll(N4JEntityType.class);
        nodeTypes.forEach(x -> {
            colors.put(x.getId(), x.getColor());
            if (x.getLabel() != null)
                labels.put(x.getId(), x.getLabel().getName());
        });

        HashSet<String> existentNodes = new HashSet<>(nodesIds);


        Parameter<String> nodeIdParam = Cypher.parameter("nodeId", nodeId);
        Node nodeX = Cypher.node("Entity").named("x");
        Node nodeY = Cypher.node("Entity").named("y");

        Statement adjacentEntitiesQueryDSL = Cypher
                .match(nodeX.relationshipBetween(nodeY))
                .where(nodeX.property("OHUUID").isEqualTo(nodeIdParam))
                .with(
                        nodeY,
                        Functions.keys(Functions.properties(nodeY)).as("attributeKeys")
                )
                .unwind(Cypher.name("attributeKeys")).as("key")
                .returning(
                        nodeY.property("OHUUID").as("id"),
                        Functions.labels(nodeY).as("labels"),
                        Functions.collect(Cypher.mapOf("name", Cypher.name("key"), "value", nodeY.property(Cypher.name("key")))).as("properties")
                )
                .build();

        var adjacentEntities = neo4jTemplate.findAll(adjacentEntitiesQueryDSL, N4JEntity.class);

        // From all adjacent nodes get the ones that are not in the graph already
        var newEntities = adjacentEntities.stream().filter(x -> !existentNodes.contains(x.getId())).toList();
        var newEntitiesIds = newEntities.stream().map(N4JEntity::getId).toList();

        Parameter<List<String>> newNodesIdsParam = Cypher.parameter("newNodesIds", newEntitiesIds);
        Parameter<List<String>> oldNodesIdsParam = Cypher.parameter("oldNodesIds", nodesIds);
        Relationship linkR = nodeX.relationshipTo(nodeY).named("r");

        Statement newOutgoingLinksQuery = Cypher
                .match(linkR)
                .where(
                        (nodeX.property("OHUUID").in(newNodesIdsParam).or(nodeX.property("OHUUID").in(oldNodesIdsParam)))
                                .and(nodeY.property("OHUUID").in(newNodesIdsParam))
                )
                .returning(
                        nodeX.property("OHUUID").as("source"),
                        nodeY.property("OHUUID").as("target"),
                        Functions.type(linkR).as("label"),
                        Functions.count(nodeX).as("value")
                ).build();

        Statement newIncomingLinksQuery = Cypher
                .match(linkR)
                .where(
                        nodeX.property("OHUUID").in(newNodesIdsParam)
                                .and(nodeY.property("OHUUID").in(oldNodesIdsParam))
                )
                .returning(
                        nodeX.property("OHUUID").as("source"),
                        nodeY.property("OHUUID").as("target"),
                        Functions.type(linkR).as("label"),
                        Functions.count(nodeX).as("value")
                ).build();

        var outgoingLinks = neo4jTemplate.findAll(newOutgoingLinksQuery, GraphLinkDTO.class);
        var incomingLinks = neo4jTemplate.findAll(newIncomingLinksQuery, GraphLinkDTO.class);

        var links = Stream.concat(outgoingLinks.stream(), incomingLinks.stream()).collect(Collectors.toList());

        var newNodes = newEntities.stream().map(n -> {
//            var properties = new ArrayList<N4JPropertyValue>();
            var properties = n.getProperties().stream().map(x ->
            {
                var propertyMap = (MapValue)x;
                return new N4JPropertyValue(propertyMap.asMap());
            }).toList();


            Map<String, Object> map = new HashMap<>();

            for (var p : properties){
                if (!map.containsKey(p.getName())){
                    map.put(p.getName(), p.getValue());
                }
            }

            var nodeType = (String)map.get("__type");
            return new GraphNodeDTO(
                n.getId(),
                    (String)map.get(labels.get(nodeType)),
                colors.get(nodeType),
                1
            );
        }).toList();

        return new GraphDTO(newNodes, links);
    }

    @Override
    public List<LinkDTO> GetGraphReferences(List<String> nodesIds){
        HashMap<String, String> labels = new HashMap<>();

        var nodeTypes = neo4jTemplate.findAll(N4JEntityType.class);

        Parameter<List<String>> nodesIdsParam = Cypher.parameter("nodesIds", nodesIds);
        Node nodeX = Cypher.node("Entity").named("x");
        Node nodeY = Cypher.node("Entity").named("y");
        Relationship referenceRelationship = nodeX.relationshipTo(nodeY).named("r");

        Case nodeXLabelCaseStatement =
                Case.create(nodeX.property("__type"));

        Case nodeYLabelCaseStatement =
                Case.create(nodeY.property("__type"));

        for (var entityType: nodeTypes) {
            nodeXLabelCaseStatement = nodeXLabelCaseStatement
                    .when(Cypher.literalOf(entityType.getId()))
                    .then(nodeX.property(entityType.getLabel().getName()));

            nodeYLabelCaseStatement = nodeYLabelCaseStatement
                    .when(Cypher.literalOf(entityType.getId()))
                    .then(nodeY.property(entityType.getLabel().getName()));
        }

        Statement getGraphReferencesQuery = Cypher
                .match(referenceRelationship)
                .where(nodeX.property("OHUUID").in(nodesIdsParam).and(nodeY.property("OHUUID").in(nodesIdsParam)))
                .returning(
                        nodeXLabelCaseStatement.as("leftEntity"),
                        nodeYLabelCaseStatement.as("rightEntity"),
                        Functions.type(referenceRelationship).as("type"),
                        referenceRelationship.property("source").as("sourceName"),
                        referenceRelationship.property("sourceurl").as("sourceUrl")
                ).build();

        return neo4jTemplate.findAll(getGraphReferencesQuery, LinkDTO.class);

    }

    @Override
    public EntityDTO GetNode(String nodeId) {

        Parameter<String> nodeIdParam = Cypher.parameter("nodeId", nodeId);
        Node entityNode = Cypher.node("Entity").named("e");
        Statement getNodeQuery = Cypher
                .match(entityNode)
                .where(entityNode.property("OHUUID").isEqualTo(nodeIdParam))
                .with(
                        entityNode,
                        Functions.keys(Functions.properties(entityNode)).as("attributeKeys")
                )
                .unwind(Cypher.name("attributeKeys")).as("key")
                .returning(
                        entityNode.property("OHUUID").as("id"),
                        Functions.labels(entityNode).as("labels"),
                        Functions.collect(Cypher.mapOf("name", Cypher.name("key"), "value", entityNode.property(Cypher.name("key")))).as("properties")
                )
                .build();

        var entity = neo4jTemplate.findOne(getNodeQuery, new HashMap<>(), N4JEntity.class);

        if (entity.isEmpty())
            return null;

        Node sourceNode = Cypher.node("Source").named("s");
        Relationship fromSourceRelationship = entityNode.relationshipTo(sourceNode, "FROM_SOURCE").named("r");

        Statement getReferencesQuery = Cypher
                .match(fromSourceRelationship)
                .where(entityNode.property("OHUUID").isEqualTo(nodeIdParam))
                .returning(
                        fromSourceRelationship.property("source").as("source"),
                        fromSourceRelationship.property("url").as("sourceUrl"),
                        fromSourceRelationship.property("externalid").as("externalId")
                ).build();

        var references = neo4jTemplate.findAll(getReferencesQuery, ReferenceDTO.class);


        Node synonymNode = Cypher.node("Synonym").named("s");
        Relationship hasSynonymRelationship = entityNode.relationshipTo(synonymNode, "HAS_SYNONYM").named("r");
        Statement getSynonymsQuery = Cypher
                .match(hasSynonymRelationship)
                .where(entityNode.property("OHUUID").isEqualTo(nodeIdParam))
                .returning(
                        synonymNode.property("name").as("name")
                ).build();

        var synonyms = neo4jTemplate.findAll(getSynonymsQuery, SynonymDTO.class);

        var entityValue = entity.get();

        var properties = entityValue.getProperties().stream().map(x ->
        {
            var propertyMap = (MapValue)x;
            return new N4JPropertyValue(propertyMap.asMap());
        }).toList();

        Map<String, Object> propertiesMap = properties.stream()
                .collect(
                        Collectors.toMap(N4JPropertyValue::getName, N4JPropertyValue::getValue));

        var nodeType = neo4jTemplate.findById(propertiesMap.get("__type"), N4JEntityType.class);

        if (nodeType.isEmpty())
            return null;

        var nodeTypeValue = nodeType.get();

        var entityDto = new EntityDTO(nodeId, nodeTypeValue.getName(), entityValue.getLabels(), nodeTypeValue.getColor());

        List<PropertyValueDTO> propertyValues = new ArrayList<>();

        for (N4JPropertyInfo p : nodeTypeValue.getProperties().stream().sorted((p1, p2) -> Integer.compare(p1.getPosition(), p2.getPosition())).toList()) {
            propertyValues.add(
                    new PropertyValueDTO(
                            p.getName(),
                            propertiesMap.get(p.getName()),
                            p.getDataType(),
                            p.getPosition()
                    )
            );
        }

        entityDto.setProperties(propertyValues);
        entityDto.setReferences(references);
        entityDto.setSynonyms(synonyms.stream().map(SynonymDTO::getName).toList());

        return entityDto;

    }

    @Override
    public GraphLinkDTO GetLink(String linkId) {
        return null;
    }

    @Override
    public List<LinkDTO> GetLinks(String sourceId, String targetId, String type) {

        Parameter<String> sourceIdParam = Cypher.parameter("sourceId", sourceId);
        Parameter<String> targetIdParam = Cypher.parameter("targetId", targetId);

        Node sourceNode = Cypher.node("Entity").named("x");
        Node targetNode = Cypher.node("Entity").named("y");
        Relationship referenceRelationship = sourceNode.relationshipBetween(targetNode).named("r");

        Statement getLinkReferencesQuery = Cypher
                .match(referenceRelationship)
                .where(sourceNode.property("OHUUID").isEqualTo(sourceIdParam).and(targetNode.property("OHUUID").isEqualTo(targetIdParam)))
                .returning(
                        sourceNode.property("OHUUID").as("leftEntity"),
                        sourceNode.property("OHUUID").as("rightEntity"),
                        Functions.type(referenceRelationship).as("type"),
                        referenceRelationship.property("source").as("sourceName"),
                        referenceRelationship.property("sourceurl").as("sourceUrl")
                ).build();

        return neo4jTemplate.findAll(getLinkReferencesQuery, LinkDTO.class);
    }

    @Override
    public List<EntitySearchResultDTO> GetByIds(List<String> ids) {
        HashMap<String, String> colors = new HashMap<>();
        HashMap<String, String> labels = new HashMap<>();
        HashMap<String, String> types = new HashMap<>();

        var nodeTypes = neo4jTemplate.findAll(N4JEntityType.class);
        nodeTypes.forEach(x -> {
            colors.put(x.getId(), x.getColor());
            if (x.getLabel() != null)
                labels.put(x.getId(), x.getLabel().getName());
            types.put(x.getId(), x.getName());
        });



        Parameter<List<String>> nodeIdsParam = Cypher.parameter("nodeIds", ids);
        Node node = Cypher.node("Entity").named("n");
        Statement queryDSL = Cypher
                .match(node)
                .where(node.property("OHUUID").in(nodeIdsParam))
                .with(
                        node,
                        Functions.keys(Functions.properties(node)).as("attributeKeys")
                )
                .unwind(Cypher.name("attributeKeys")).as("key")
                .returning(
                        node.property("OHUUID").as("id"),
                        Functions.labels(node).as("labels"),
                        Functions.collect(Cypher.mapOf("name", Cypher.name("key"), "value", node.property(Cypher.name("key")))).as("properties")
                )
                .build();

        var matchedEntities = neo4jTemplate.findAll(queryDSL, N4JEntity.class);


        var newNodes = matchedEntities.stream().map(n -> {
//            var properties = new ArrayList<N4JPropertyValue>();
            var properties = n.getProperties().stream().map(x ->
            {
                var propertyMap = (MapValue)x;
                return new N4JPropertyValue(propertyMap.asMap());
            }).toList();


            Map<String, Object> map = new HashMap<>();

            for (var p : properties){
                if (!map.containsKey(p.getName())){
                    map.put(p.getName(), p.getValue());
                }
            }

            var nodeType = (String)map.get("__type");
            return new EntitySearchResultDTO(
                    n.getId(),
                    (String)map.get(labels.get(nodeType)),
                    types.get(nodeType),
                    colors.get(nodeType),
                    n.getId()
            );
        }).toList();

        return newNodes;
    }

}
