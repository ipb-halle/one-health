package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphLinkDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphNodeDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IOntologyRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IEntityService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public GraphDTO GetAdjacentNodes(String nodeId) {
        return entityRepository.GetAdjacentNodes(nodeId);
    }

    @Override
    public GraphNodeDTO GetNode(String nodeId) {
        return null;
    }

    @Override
    public GraphLinkDTO GetLink(String nodeId) {
        return null;
    }
}
