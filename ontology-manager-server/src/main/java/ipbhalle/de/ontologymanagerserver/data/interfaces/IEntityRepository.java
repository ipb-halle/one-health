package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntitySearchResultDTO;

import java.util.List;

public interface IEntityRepository extends IGraphRepository{
    List<EntitySearchResultDTO> GetByIds(List<String> ids);
}
