package de.ipb_halle.server.n4j.repository;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import de.ipb_halle.server.data.dtos.LinkTypeDTO;
import de.ipb_halle.server.data.interfaces.ILinkTypeRepository;
import de.ipb_halle.server.n4j.mapping.N4JMapper;
import de.ipb_halle.server.n4j.models.N4JEntityType;
import de.ipb_halle.server.n4j.models.N4JLinkType;
import de.ipb_halle.server.services.interfaces.PageResult;
import de.ipb_halle.server.services.interfaces.QueryCommand;

import java.util.List;

@Repository
public class N4JLinkTypeRepository implements ILinkTypeRepository {

    private final Neo4jTemplate neo4jTemplate;

    N4JLinkTypeRepository(Neo4jTemplate neo4jTemplate){
        this.neo4jTemplate = neo4jTemplate;
    }


    @Override
    public LinkTypeDTO Create(LinkTypeDTO dto) {
        N4JLinkType entity = N4JMapper.MAPPER.map(dto);
        var leftEntityType = neo4jTemplate.findById(dto.getLeftEntityTypeId(), N4JEntityType.class);
        var rightEntityType = neo4jTemplate.findById(dto.getRightEntityTypeId(), N4JEntityType.class);



        entity.setLeftEntityType(leftEntityType.get());
        entity.setRightEntityType(rightEntityType.get());


        entity = neo4jTemplate.save(entity);
        return N4JMapper.MAPPER.map(entity);
    }

    @Override
    public List<LinkTypeDTO> CreateAll(Iterable<LinkTypeDTO> dtos) {
        return List.of();
    }

    @Override
    public LinkTypeDTO Update(LinkTypeDTO dto) {
        return null;
    }

    @Override
    public LinkTypeDTO Get(String id) {
        var result = neo4jTemplate.findById(id, N4JLinkType.class);

        if (result.isEmpty())
            throw new RuntimeException(); // TODO: customize this
        return N4JMapper.MAPPER.map(result.get());
    }

    @Override
    public List<LinkTypeDTO> GetAll() {
        return neo4jTemplate.findAll(N4JLinkType.class).stream().map(N4JMapper.MAPPER::map).toList();
    }

    @Override
    public void Delete(String id) {

    }

    @Override
    public PageResult<LinkTypeDTO> GetPage(QueryCommand query) {
        return null;
    }
}
