package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;

import java.util.List;

public interface IGraphRepository {

    GraphDTO GetInitialSet();
    GraphDTO GetAdjacentNodes(String nodeId, List<String> nodes);
    EntityDTO GetNode(String nodeId);
    GraphLinkDTO GetLink(String linkId);
    List<LinkDTO> GetLinks (String sourceId, String targetId, String type);
}
