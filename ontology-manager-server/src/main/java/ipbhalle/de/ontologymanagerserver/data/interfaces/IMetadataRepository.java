package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.MetadataElementDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.MetadataSummaryDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.SelectableOption;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IMetadataRepository {
    GraphDTO GetAll();
    MetadataElementDTO GetEntityType(String id);
    MetadataElementDTO GetLinkType(String id);
    MetadataSummaryDTO GetSummary ();
    List<SelectableOption<String>> getAllAsOptions();

}
