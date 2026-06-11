package de.ipb_halle.server.data.interfaces;

import de.ipb_halle.server.data.dtos.EntityTypeDTO;

public abstract class IDataContext {
    private IPagedDataRepository<EntityTypeDTO, EntityTypeDTO, String> entityTypes;

}
