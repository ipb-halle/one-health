package de.ipb_halle.server.services.interfaces;

import org.springframework.stereotype.Service;

import de.ipb_halle.server.data.interfaces.DTO;

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
