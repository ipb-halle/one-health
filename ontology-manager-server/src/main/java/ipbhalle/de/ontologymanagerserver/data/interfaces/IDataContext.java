package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntityDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;

public abstract class IDataContext {
    private IPagedDataRepository<EntityTypeDTO, EntityTypeDTO, String> entityTypes;

}
