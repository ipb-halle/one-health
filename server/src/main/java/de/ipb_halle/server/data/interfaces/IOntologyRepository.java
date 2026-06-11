package de.ipb_halle.server.data.interfaces;

import java.util.List;

import de.ipb_halle.server.data.dtos.CoOcurrenceQuery;
import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.LinkDTO;

public interface IOntologyRepository extends IGraphRepository {
    List<EntityDTO> GetAsGraph();

    GraphDTO FindCoOccurrences(CoOcurrenceQuery query);
    List<LinkDTO> FindCoOccurrencesDetails(CoOcurrenceQuery query);
}
