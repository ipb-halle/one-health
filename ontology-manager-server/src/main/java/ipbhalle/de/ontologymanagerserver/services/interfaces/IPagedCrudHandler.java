package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;

public interface IPagedCrudHandler<TDTO extends DTO<TKey>, TPageDTO extends DTO<TKey>, TKey extends Comparable<TKey>>
       extends ICrudHandler<TDTO, TKey>, IDataHandler {
    PageResult<TDTO> GetPage(QueryCommand queryCommand);

}
