package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;

public interface IGraphService {
    GraphDTO GetInitialSet();
    GraphDTO GetAdjacentNodes(String nodeId);
}
