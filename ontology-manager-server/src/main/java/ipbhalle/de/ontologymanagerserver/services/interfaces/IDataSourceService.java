package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.DataSourceDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeMappingDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.LinkTypeMappingDTO;

// TODO : add default implementation in the service classes to remove the code from the services
public interface IDataSourceService extends IPagedCrudHandler<DataSourceDTO, DataSourceDTO, String>{

    void WriteFile(String fileId, String content);

    String MapEntities(String id, EntityTypeMappingDTO mapping);

    String MapLinks(String id, LinkTypeMappingDTO mapping);

}
