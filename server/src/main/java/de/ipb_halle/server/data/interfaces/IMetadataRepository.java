package de.ipb_halle.server.data.interfaces;

import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.MetadataElementDTO;
import de.ipb_halle.server.data.dtos.MetadataSummaryDTO;
import de.ipb_halle.server.data.dtos.SelectableOption;

import java.util.List;

public interface IMetadataRepository {
    GraphDTO GetAll();
    MetadataElementDTO GetEntityType(String id);
    MetadataElementDTO GetLinkType(String id);
    MetadataSummaryDTO GetSummary ();
    List<SelectableOption<String>> getAllAsOptions();

}
