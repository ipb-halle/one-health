package de.ipb_halle.server.services.interfaces;

import de.ipb_halle.server.data.dtos.LinkTypeDTO;

public interface ILinkTypeService {
	PageResult<LinkTypeDTO> GetPage(QueryCommand query);
}
