package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IOntologyRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IEntityService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public EntityDTO GetNode(String nodeId) {
        return entityRepository.GetNode(nodeId);
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
