package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.SelectableOption;

import java.util.Collection;
import java.util.List;

public interface IEntityTypeRepository extends IPagedDataRepository<EntityTypeDTO, EntityTypeDTO, String>
{
    List<SelectableOption<String>> getEntityTypeAsOptions();
}
