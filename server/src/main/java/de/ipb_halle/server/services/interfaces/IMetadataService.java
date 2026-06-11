package de.ipb_halle.server.services.interfaces;

import java.util.List;

import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.MetadataElementDTO;
import de.ipb_halle.server.data.dtos.MetadataSummaryDTO;
import de.ipb_halle.server.data.dtos.SelectableOption;

public interface IMetadataService {
    GraphDTO GetAll();

    MetadataElementDTO GetEntityType(String id);
    MetadataElementDTO GetLinkType(String id);

    MetadataSummaryDTO GetSummary();
    List<SelectableOption<String>> GetAllAsOptions();
}
