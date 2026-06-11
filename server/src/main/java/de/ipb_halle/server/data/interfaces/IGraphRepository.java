package de.ipb_halle.server.data.interfaces;

import java.util.List;

import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.GraphLinkDTO;
import de.ipb_halle.server.data.dtos.LinkDTO;
import de.ipb_halle.server.data.dtos.*;

public interface IGraphRepository {

    GraphDTO GetInitialSet();
    GraphDTO GetAdjacentNodes(String nodeId, List<String> nodes);
    List<EntityDTO> GetNodes(List<String> nodeIds);
    GraphLinkDTO GetLink(String linkId);
    List<LinkDTO> GetLinks (String sourceId, String targetId, String type);
}
