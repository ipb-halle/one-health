package de.ipb_halle.server.services.interfaces;

import de.ipb_halle.server.data.interfaces.DTO;

public interface IPagedCrudHandler<TDTO extends DTO<TKey>, TPageDTO extends DTO<TKey>, TKey extends Comparable<TKey>>
       extends ICrudHandler<TDTO, TKey>, IDataHandler {
    PageResult<TDTO> GetPage(QueryCommand queryCommand);

}
