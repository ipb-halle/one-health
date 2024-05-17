package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphLinkDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphNodeDTO;

public interface IGraphService {
    GraphDTO GetInitialSet();
    GraphDTO GetAdjacentNodes(String nodeId);
    GraphNodeDTO GetNode(String nodeId);
    GraphLinkDTO GetLink(String nodeId);
}
