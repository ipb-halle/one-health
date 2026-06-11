package de.ipb_halle.server.services.interfaces;

import java.util.List;

import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.GraphLinkDTO;
import de.ipb_halle.server.data.dtos.LinkDTO;

public interface IGraphService {
    GraphDTO GetInitialSet();
    GraphDTO GetAdjacentNodes(String nodeId, List<String> nodes);
    List<EntityDTO> GetNodes(List<String> nodeIds);
    GraphLinkDTO GetLink(String nodeId);
    List<LinkDTO> GetLinks (String sourceId, String targetId, String type);
}
