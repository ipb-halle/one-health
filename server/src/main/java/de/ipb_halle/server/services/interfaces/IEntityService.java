package de.ipb_halle.server.services.interfaces;

import java.util.List;

import de.ipb_halle.server.data.dtos.LinkDTO;

public interface IEntityService extends IGraphService{
    List<LinkDTO> GetGraphReferences(List<String> nodesIds);
}
