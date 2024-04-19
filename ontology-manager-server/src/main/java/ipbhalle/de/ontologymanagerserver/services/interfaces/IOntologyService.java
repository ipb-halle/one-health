package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.CoOcurrenceQuery;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;

import java.util.List;

public interface IOntologyService extends IGraphService {
    List<EntityDTO> GetAsGraph();

    GraphDTO FindCoOccurrences(CoOcurrenceQuery query);
}
