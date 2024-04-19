package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.services.interfaces.PageResult;
import ipbhalle.de.ontologymanagerserver.services.interfaces.QueryCommand;

public interface IPagedDataRepository<TDTO extends DTO<TKey>, TPageDTO extends DTO<TKey> ,TKey extends Comparable<TKey>>
    extends IDataRepository<TDTO, TKey>
{
    PageResult<TDTO> GetPage(QueryCommand query);
}
