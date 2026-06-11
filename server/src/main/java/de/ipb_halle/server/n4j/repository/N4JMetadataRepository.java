package de.ipb_halle.server.n4j.repository;

import de.ipb_halle.server.data.dtos.*;
import de.ipb_halle.server.data.dtos.*;
import org.springframework.data.neo4j.core.Neo4jOperations;
import org.springframework.stereotype.Repository;

import de.ipb_halle.server.data.interfaces.IMetadataRepository;
import de.ipb_halle.server.n4j.mapping.N4JMapper;
import de.ipb_halle.server.n4j.models.N4JEntityType;
import de.ipb_halle.server.n4j.models.N4JLinkType;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class N4JMetadataRepository implements IMetadataRepository {

    private final Neo4jOperations neo4jOperations;

    N4JMetadataRepository(Neo4jOperations neo4jOperations){
        this.neo4jOperations = neo4jOperations;
    }

    @Override
    public GraphDTO GetAll() {
        var entityTypes = neo4jOperations.findAll(N4JEntityType.class);
        var linkTypes = neo4jOperations.findAll(N4JLinkType.class);

        String cypherQuery = "MATCH (source)-[link:PARENT_OF]->(target) RETURN \"PARENT_OF\" AS id, \"IS_PARENT_OF\" AS label, source.id AS source, target.id AS target, \"OUTGOING\" AS direction";
        var inheritanceLinks = neo4jOperations.findAll(cypherQuery, GraphLinkDTO.class);


        return new GraphDTO(
                entityTypes.stream().map(x -> new GraphNodeDTO(
                    x.getId(),
                    x.getName(),
                    x.getColor(),
                    1
                )).collect(Collectors.toList()),


                Stream.concat(
                        linkTypes.stream().map(x -> new GraphLinkDTO(x.getId(), x.getName(),
                                x.getLeftEntityType().getId(),
                                x.getRightEntityType().getId(),
                                x.getDirection(),
                                1
                        )), inheritanceLinks.stream()).collect(Collectors.toList())




        );

    }

    @Override
    public MetadataElementDTO GetEntityType(String id) {
        var result = neo4jOperations.findById(id, N4JEntityType.class);
        if (result.isEmpty())
            throw new RuntimeException(); // TODO: customize
        return N4JMapper.MAPPER.EntityTypeToElement(result.get());
    }

    @Override
    public MetadataElementDTO GetLinkType(String id) {
        var result = neo4jOperations.findById(id, N4JLinkType.class);
        if (result.isEmpty())
            throw new RuntimeException(); // TODO: customize
        return N4JMapper.MAPPER.LinkTypeToElement(result.get());
    }

    @Override
    public MetadataSummaryDTO GetSummary() {
        var entityTypeCount = neo4jOperations.count(N4JEntityType.class);
        var linkTypeCount = neo4jOperations.count(N4JLinkType.class);
        var entitiesCount = neo4jOperations.count("match(n:Entity) return count(n)");
        var linksCount = neo4jOperations.count("match (:Entity) - [r] - (:Entity) return count(r) / 2");
        return new MetadataSummaryDTO(entityTypeCount, entitiesCount, linkTypeCount, linksCount, 0, 0);
    }

    @Override
    public List<SelectableOption<String>> getAllAsOptions() {
        var entityTypes = neo4jOperations.findAll(N4JEntityType.class);
        var linkTypes = neo4jOperations.findAll(N4JLinkType.class);

        var entityPayload = new HashMap<String, String>();
        entityPayload.put("type", "entity");
        var linkPayload = new HashMap<String, String>();
        linkPayload.put("type", "link");


        var all = Stream.concat(
                entityTypes.stream().map(x -> new SelectableOption<String>(x.getName(), x.getId(), entityPayload)),
                linkTypes.stream().map(x -> new SelectableOption<String>(
                        String.format("(%s) - [%s] - (%s)", x.getLeftEntityType().getName(), x.getRightEntityType().getName(), x.getName()),
                        x.getId(),
                        linkPayload
                ))
        ).toList();

        return all;
    }
}
