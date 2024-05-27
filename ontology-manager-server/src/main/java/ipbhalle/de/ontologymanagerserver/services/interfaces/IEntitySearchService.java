package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntitySearchResultDTO;

import java.util.List;

public interface IEntitySearchService {
    List<EntitySearchResultDTO> FindEntities(String query);

}
