package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.KeywordDTO;

public interface IKeywordRepository extends IDataRepository<KeywordDTO, String> {
    KeywordDTO GetByValue(String value);
}
