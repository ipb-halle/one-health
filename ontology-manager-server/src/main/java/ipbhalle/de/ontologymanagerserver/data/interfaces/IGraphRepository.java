package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;

public interface IGraphRepository {

    GraphDTO GetInitialSet();
    GraphDTO GetAdjacentNodes(String nodeId);

}
