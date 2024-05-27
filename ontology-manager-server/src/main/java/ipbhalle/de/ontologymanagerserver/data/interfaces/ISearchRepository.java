package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.postgre.models.PSIndexEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISearchRepository extends JpaRepository<PSIndexEntry, Long> {
}
