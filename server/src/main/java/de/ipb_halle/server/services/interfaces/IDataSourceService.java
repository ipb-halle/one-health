package de.ipb_halle.server.services.interfaces;

import de.ipb_halle.server.data.dtos.DataSourceDTO;
import de.ipb_halle.server.data.dtos.EntityTypeMappingDTO;
import de.ipb_halle.server.data.dtos.LinkTypeMappingDTO;

// TODO : add default implementation in the service classes to remove the code from the services
public interface IDataSourceService extends IPagedCrudHandler<DataSourceDTO, DataSourceDTO, String>{

    void WriteFile(String fileId, String content);

    String MapEntities(String id, EntityTypeMappingDTO mapping);

    String MapLinks(String id, LinkTypeMappingDTO mapping);

}
