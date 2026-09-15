package de.ipb_halle.server.data.interfaces;

import de.ipb_halle.server.data.dtos.LinkTypeDTO;
import de.ipb_halle.server.services.interfaces.PageResult;
import de.ipb_halle.server.services.interfaces.QueryCommand;

public interface ILinkTypeRepository {
	LinkTypeDTO Create(LinkTypeDTO dto);
	PageResult<LinkTypeDTO> GetPage(QueryCommand query);
}
