package de.ipb_halle.server.data.interfaces;

import de.ipb_halle.server.data.dtos.LinkTypeDTO;

public interface ILinkTypeRepository {
	LinkTypeDTO Create(LinkTypeDTO dto);
}
