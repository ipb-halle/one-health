package ipbhalle.de.ontologymanagerserver.data.interfaces;

import java.util.List;

public interface IDataRepository<TDTO extends DTO<TKey>, TKey extends Comparable<TKey>> {
    TDTO Create(TDTO dto);
    List<TDTO> CreateAll(Iterable<TDTO> dtos);
    TDTO Update(TDTO dto);
    TDTO Get(TKey id);
    List<TDTO> GetAll();
    void Delete(TKey id);
}
