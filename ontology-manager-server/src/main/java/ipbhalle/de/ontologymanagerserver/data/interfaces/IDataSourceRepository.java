package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.DataSourceDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeMappingDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.LinkTypeMappingDTO;

public interface IDataSourceRepository extends IPagedDataRepository<DataSourceDTO, DataSourceDTO, String> {

    String MapEntities(String id, EntityTypeMappingDTO mapping);

    String MapLinks(String id, LinkTypeMappingDTO mapping);
}
