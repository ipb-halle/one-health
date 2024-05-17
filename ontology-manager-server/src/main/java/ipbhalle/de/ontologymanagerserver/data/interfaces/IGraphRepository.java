package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphLinkDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphNodeDTO;

public interface IGraphRepository {

    GraphDTO GetInitialSet();
    GraphDTO GetAdjacentNodes(String nodeId);
    GraphNodeDTO GetNode(String nodeId);
    GraphLinkDTO GetLink(String linkId);

}
