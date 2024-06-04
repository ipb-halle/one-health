package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.LinkDTO;

import java.util.List;

public interface IEntityService extends IGraphService{
    List<LinkDTO> GetGraphReferences(List<String> nodesIds);
}
