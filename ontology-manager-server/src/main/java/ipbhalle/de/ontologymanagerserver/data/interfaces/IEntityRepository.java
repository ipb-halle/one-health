package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntitySearchResultDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.LinkDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.NaturalProductDTO;

import java.util.List;
import java.util.Map;

public interface IEntityRepository extends IGraphRepository{
    List<EntitySearchResultDTO> GetByIds(List<String> ids);
    List<LinkDTO> GetGraphReferences(List<String> nodesIds);
}
