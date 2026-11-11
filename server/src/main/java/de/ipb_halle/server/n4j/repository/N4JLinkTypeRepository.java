package de.ipb_halle.server.n4j.repository;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import de.ipb_halle.server.data.dtos.LinkTypeDTO;
import de.ipb_halle.server.data.interfaces.ILinkTypeRepository;
import de.ipb_halle.server.n4j.mapping.N4JMapper;
import de.ipb_halle.server.n4j.models.N4JEntityType;
import de.ipb_halle.server.n4j.models.N4JLinkType;

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


}
