package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.SelectableOption;
import java.util.List;

public interface IEntityTypeService extends IPagedCrudHandler<EntityTypeDTO, EntityTypeDTO, String> {

    List<SelectableOption<String>> getEntityTypeAsOptions();

}
