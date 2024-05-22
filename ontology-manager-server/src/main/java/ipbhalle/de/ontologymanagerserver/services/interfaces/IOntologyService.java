package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;

import java.util.List;

public interface IOntologyService extends IGraphService {
    List<EntityDTO> GetAsGraph();

    GraphDTO FindCoOccurrences(CoOcurrenceQuery query);
    List<LinkDTO> FindCoOccurrencesDetails(CoOcurrenceQuery query);
}
