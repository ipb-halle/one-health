package de.ipb_halle.server.data.interfaces;

import de.ipb_halle.server.services.interfaces.PageResult;
import de.ipb_halle.server.services.interfaces.QueryCommand;

public interface IPagedDataRepository<TDTO extends DTO<TKey>, TPageDTO extends DTO<TKey> ,TKey extends Comparable<TKey>>
    extends IDataRepository<TDTO, TKey>
{
    PageResult<TDTO> GetPage(QueryCommand query);
}
