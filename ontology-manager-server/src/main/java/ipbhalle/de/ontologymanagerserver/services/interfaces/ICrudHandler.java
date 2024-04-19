package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICrudHandler<TDTO extends DTO<TKey>, TKey extends Comparable<TKey>>
{
    TDTO Create(TDTO dto);
    TDTO Update(TDTO dto);
    TDTO Get(TKey id);
    List<TDTO> GetAll();
    void Delete(TKey id);

}
