package de.ipb_halle.server.services;

import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.GraphLinkDTO;
import de.ipb_halle.server.data.dtos.LinkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ipb_halle.server.data.interfaces.IEntityRepository;
import de.ipb_halle.server.services.interfaces.IEntityService;

import java.util.List;

@Service
public class EntityService implements IEntityService {

    private final IEntityRepository entityRepository;

    @Autowired
    public EntityService(IEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public GraphDTO GetInitialSet() {
        return entityRepository.GetInitialSet();
    }

    @Override
    public GraphDTO GetAdjacentNodes(String nodeId, List<String> nodes) {
        return entityRepository.GetAdjacentNodes(nodeId, nodes);
    }

    @Override
    public List<EntityDTO> GetNodes(List<String> nodeIds) {
        return entityRepository.GetNodes(nodeIds);
    }

    @Override
    public GraphLinkDTO GetLink(String nodeId) {
        return null;
    }

    @Override
    public List<LinkDTO> GetLinks(String sourceId, String targetId, String type) {
        return entityRepository.GetLinks(sourceId, targetId, type);
    }

    @Override
    public List<LinkDTO> GetGraphReferences(List<String> nodesIds){
        return entityRepository.GetGraphReferences(nodesIds);
    }
}
