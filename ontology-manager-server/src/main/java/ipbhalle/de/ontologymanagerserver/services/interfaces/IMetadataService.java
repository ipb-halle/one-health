package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.MetadataElementDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.MetadataSummaryDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.SelectableOption;

import java.util.List;

public interface IMetadataService {
    GraphDTO GetAll();

    MetadataElementDTO GetEntityType(String id);
    MetadataElementDTO GetLinkType(String id);

    MetadataSummaryDTO GetSummary();
    List<SelectableOption<String>> GetAllAsOptions();
}
