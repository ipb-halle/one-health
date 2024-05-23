package ipbhalle.de.ontologymanagerserver.n4j.repository;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.PropertyInfoDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.SelectableOption;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityTypeRepository;
import ipbhalle.de.ontologymanagerserver.n4j.mapping.N4JMapper;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JEntityType;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JPropertyInfo;
import ipbhalle.de.ontologymanagerserver.services.interfaces.PageResult;
import ipbhalle.de.ontologymanagerserver.services.interfaces.QueryCommand;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
public class N4JEntityTypeRepository implements IEntityTypeRepository {

    private final Neo4jTemplate neo4jTemplate;

    N4JEntityTypeRepository(Neo4jTemplate neo4jTemplate){
        this.neo4jTemplate = neo4jTemplate;
    }

    @Override
    public EntityTypeDTO Create(EntityTypeDTO dto) {
        N4JEntityType entity = N4JMapper.MAPPER.map(dto);

        if (entity.getParent() != null) {
            // Check that the parent exists
            Optional<N4JEntityType> parentOption = neo4jTemplate.findById(dto.getParent().getId(), N4JEntityType.class);
            if (parentOption.isEmpty())
                throw new RuntimeException("The parent Entity Type doesn't exists.");

            var parent = parentOption.get();
            entity.setParent(parent);
            var inheritedProperties = Stream.concat(parent.getProperties().stream(), parent.getInheritedProperties().stream()).collect(Collectors.toSet());
            // Make sure that the inherited properties of the child entity type are all the properties
            // (both inherited and own) of the parent
            entity.setInheritedProperties(inheritedProperties);
        }

        var label = dto.getProperties().stream().filter(PropertyInfoDTO::isLabel).findFirst();

        N4JPropertyInfo labelprop = N4JMapper.MAPPER.map(label.get());


        entity.setLabel(labelprop);

        entity = neo4jTemplate.save(entity);
        return N4JMapper.MAPPER.map(entity);
    }

    @Override
    public List<EntityTypeDTO> CreateAll(Iterable<EntityTypeDTO> dtos) {
        return List.of();
    }

    @Override
    public EntityTypeDTO Update(EntityTypeDTO dto) {
        //TODO: This should be a transaction
        N4JEntityType newEntity = N4JMapper.MAPPER.map(dto);

        if (dto.getId() == null)
            throw new RuntimeException(); // TODO: Add message

        Optional<N4JEntityType> oldEntity = neo4jTemplate.findById(dto.getId(), N4JEntityType.class);

        if (oldEntity.isEmpty())
            throw new RuntimeException(); // TODO: Add message

        // We need to manually delete the non-inherited PropertyInfo objects that aren't connected to the Entity Type

        Set<String> oldProperties = oldEntity.get().getProperties().stream().map(N4JPropertyInfo::getId).collect(Collectors.toSet());
        Set<String> newProperties = newEntity.getProperties().stream().map(N4JPropertyInfo::getId).collect(Collectors.toSet());
        oldProperties.removeAll(newProperties);

        neo4jTemplate.deleteAllById(oldProperties, N4JPropertyInfo.class);
        // TODO: In the future we will also need to delete all the property values linked to the property info
        // TODO: Better to create a method in the Property Info Context

        newEntity = neo4jTemplate.save(newEntity);
        return N4JMapper.MAPPER.map(newEntity);
    }

    @Override
    public EntityTypeDTO Get(String id) {
        Optional<N4JEntityType> entity = neo4jTemplate.findById(id, N4JEntityType.class);
        return entity.isPresent() ? N4JMapper.MAPPER.map(entity.get()) : null;
    }

    @Override
    public List<EntityTypeDTO> GetAll() {
        return neo4jTemplate.findAll(N4JEntityType.class).stream().map(N4JMapper.MAPPER::map).toList();
    }

    @Override
    public void Delete(String id) {
        //TODO: This should be a transactino
        Optional<N4JEntityType> entity = neo4jTemplate.findById(id, N4JEntityType.class);

        if (entity.isEmpty())
            throw new RuntimeException(); // TODO: Add custom exception

        neo4jTemplate.deleteAllById(
                entity.get().getProperties().stream().map(N4JPropertyInfo::getId).collect(Collectors.toList()),
                N4JPropertyInfo.class
        );

        neo4jTemplate.deleteById(id, N4JEntityType.class);

    }

    @Override
    public PageResult<EntityTypeDTO> GetPage(QueryCommand query) {
        return null;
    }

    @Override
    public List<SelectableOption<String>> getEntityTypeAsOptions() {
        List<N4JEntityType> entityTypes = neo4jTemplate.findAll(N4JEntityType.class);
        return entityTypes.stream().map(x -> new SelectableOption<String>(x.getName(), x.getId())).toList();
    }
}
