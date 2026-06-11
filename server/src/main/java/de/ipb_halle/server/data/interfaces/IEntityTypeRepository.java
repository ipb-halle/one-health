package de.ipb_halle.server.data.interfaces;

import java.util.Collection;
import java.util.List;

import de.ipb_halle.server.data.dtos.EntityTypeDTO;
import de.ipb_halle.server.data.dtos.SelectableOption;

public interface IEntityTypeRepository extends IPagedDataRepository<EntityTypeDTO, EntityTypeDTO, String>
{
    List<SelectableOption<String>> getEntityTypeAsOptions();
}
