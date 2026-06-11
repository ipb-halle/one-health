package de.ipb_halle.server.data.interfaces;

import de.ipb_halle.server.data.dtos.KeywordDTO;

public interface IKeywordRepository extends IDataRepository<KeywordDTO, String> {
    KeywordDTO GetByValue(String value);
}
