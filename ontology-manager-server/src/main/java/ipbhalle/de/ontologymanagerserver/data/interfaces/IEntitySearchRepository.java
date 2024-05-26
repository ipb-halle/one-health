package ipbhalle.de.ontologymanagerserver.data.interfaces;

import java.util.List;

public interface IEntitySearchRepository {
    List<String> FindMatchingEntityIds(String query);
}
