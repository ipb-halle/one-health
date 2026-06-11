package de.ipb_halle.server.services.interfaces;

import java.util.List;

import de.ipb_halle.server.data.dtos.EntityTypeDTO;
import de.ipb_halle.server.data.dtos.SelectableOption;

public interface IEntityTypeService extends IPagedCrudHandler<EntityTypeDTO, EntityTypeDTO, String> {

    List<SelectableOption<String>> getEntityTypeAsOptions();

}
