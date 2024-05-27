package ipbhalle.de.ontologymanagerserver.n4j.repository;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IGraphRepository;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JEntity;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JEntityType;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JPropertyInfo;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JPropertyValue;
import org.neo4j.driver.internal.value.MapValue;
import org.neo4j.driver.util.Pair;
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

        // Get adjacent nodes to the expanded node
        String adjacentEntitiesQuery = "match(n) - [r] - (m:Entity) where id(n) = "
                + nodeId +
                " with m, keys(properties(m)) AS attributeKeys UNWIND attributeKeys AS key return toString(id(m)) as id, labels(m) as labels,   collect({name: key, value: properties(m)[key]}) AS properties;";
        var adjacentEntities = neo4jTemplate.findAll(adjacentEntitiesQuery, N4JEntity.class);

        // From all adjacent nodes get the ones that are not in the graph already
        var newEntities = adjacentEntities.stream().filter(x -> !existentNodes.contains(x.getId())).toList();
        var newEntitiesIds = newEntities.stream().map(N4JEntity::getId).toList();


        String newOutgoingLinksQuery =
                "with [" + String.join(", ", newEntitiesIds) + "] as newNodesIds, " +
                "[" + String.join(", ", nodesIds) + "] as nodesIds " +
                "match(n) - [r] -> (m:Entity) where " +
                        "(id(n) in newNodesIds or id(n) in nodesIds) and id(m) in newNodesIds " +
                        "with toString(id(n)) as source, toString(id(m)) as target, type(r) as label, count(r) as value" +
                " return source, target, label, value;";

        String newIncomingLinksQuery =
                "with [" + String.join(", ", newEntitiesIds) + "] as newNodesIds, " +
                        "[" + String.join(", ", nodesIds) + "] as nodesIds " +
                        "match (m:Entity) - [r] -> (n) where " +
                        "id(n) in nodesIds and id(m) in newNodesIds " +
                        "with toString(id(n)) as target, toString(id(m)) as source, type(r) as label, count(r) as value" +
                        " return source, target, label, value;";


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
    public EntityDTO GetNode(String nodeId) {
        String nodeQuery = "match (m:Entity) where id(m) = "
                + nodeId +
                " with m, keys(properties(m)) AS attributeKeys UNWIND attributeKeys AS key return toString(id(m)) as id, labels(m) as labels,   collect({name: key, value: properties(m)[key]}) AS properties;";

        var entity = neo4jTemplate.findOne(nodeQuery,new HashMap<>(), N4JEntity.class);


        String referencesQuery = "match (m:Entity) -[r:FROM_SOURCE]-> (n:Source) where id(m) = " + nodeId +
                " return r.source as source, r.url as sourceUrl, r.externalid as externalId";

        var references = neo4jTemplate.findAll(referencesQuery, ReferenceDTO.class);

        if (entity.isEmpty())
            return null;

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

        return entityDto;

    }

    @Override
    public GraphLinkDTO GetLink(String linkId) {
        return null;
    }

    @Override
    public List<LinkDTO> GetLinks(String sourceId, String targetId, String type) {
        String query = "match (n)-[r]-(m) where id(n) = " + sourceId + " and id(m) = " + targetId;
        if (type != null)
            query += " and type(r) = '" + type + "'";
        query += " return toString(id(n)) as rightEntity, toString(id(m)) as leftEntity, type(r) as type, r.source as sourceName, r.sourceurl as sourceUrl";

        return neo4jTemplate.findAll(query, LinkDTO.class);
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

        var stringifyIds = ids.stream().map(x -> "'" + x + "'").toList();

        String query =
        "with [" + String.join(", ", stringifyIds)  + "] as matches" +
                " match(m:Entity) where m.OHUUID in matches" +
                " with m, keys(properties(m)) AS attributeKeys UNWIND attributeKeys AS key return toString(id(m)) as id, labels(m) as labels,   collect({name: key, value: properties(m)[key]}) AS properties;";
        var matchedEntities = neo4jTemplate.findAll(query, N4JEntity.class);


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

    public List<NaturalProductDTO> GetIds(List<String> uuids){
        var stringifyIds = uuids.stream().map(x -> "'" + x + "'").toList();

        var query =
              "with [" + String.join(", ", stringifyIds)  + "] as matches" +
                      " match(m:`Natural Product`) where m.OHUUID in matches" +
                      " return m.Name as name, m.InChI as inChI, m.`InChI Key` as inChIKey, m.`SMILES` as smiles, m.`Molecular Formula` as molecularFormula, toFloat(m.`Molecular Weight`) as molecularWeight, m.`Cas Registry Number` as casRegistryNumber, m.`IUPAC Name` as iupacName , toString(id(m)) as id";


        return neo4jTemplate.findAll(query, NaturalProductDTO.class);

    }
}
