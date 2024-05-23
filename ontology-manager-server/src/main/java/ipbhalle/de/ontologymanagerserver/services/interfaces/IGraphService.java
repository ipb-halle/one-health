package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;

import java.util.List;

public interface IGraphService {
    GraphDTO GetInitialSet();
    GraphDTO GetAdjacentNodes(String nodeId, List<String> nodes);
    EntityDTO GetNode(String nodeId);
    GraphLinkDTO GetLink(String nodeId);
    List<LinkDTO> GetLinks (String sourceId, String targetId, String type);
}
