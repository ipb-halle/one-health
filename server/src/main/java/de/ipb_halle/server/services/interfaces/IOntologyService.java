package de.ipb_halle.server.services.interfaces;

import java.util.List;

import de.ipb_halle.server.data.dtos.CoOcurrenceQuery;
import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.LinkDTO;
import de.ipb_halle.server.data.dtos.*;

public interface IOntologyService extends IGraphService {
    List<EntityDTO> GetAsGraph();

    GraphDTO FindCoOccurrences(CoOcurrenceQuery query);
    List<LinkDTO> FindCoOccurrencesDetails(CoOcurrenceQuery query);
}
