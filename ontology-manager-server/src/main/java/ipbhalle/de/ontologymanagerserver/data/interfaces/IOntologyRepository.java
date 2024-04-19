package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.CoOcurrenceQuery;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;

import java.util.List;

public interface IOntologyRepository extends IGraphRepository {
    List<EntityDTO> GetAsGraph();

    GraphDTO FindCoOccurrences(CoOcurrenceQuery query);
}
