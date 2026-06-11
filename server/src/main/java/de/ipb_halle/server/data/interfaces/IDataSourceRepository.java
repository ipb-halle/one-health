package de.ipb_halle.server.data.interfaces;

import de.ipb_halle.server.data.dtos.DataSourceDTO;
import de.ipb_halle.server.data.dtos.EntityTypeMappingDTO;
import de.ipb_halle.server.data.dtos.LinkTypeMappingDTO;

public interface IDataSourceRepository extends IPagedDataRepository<DataSourceDTO, DataSourceDTO, String> {

    String MapEntities(String id, EntityTypeMappingDTO mapping);

    String MapLinks(String id, LinkTypeMappingDTO mapping);
}
