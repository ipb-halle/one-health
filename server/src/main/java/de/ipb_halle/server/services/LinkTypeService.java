package de.ipb_halle.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ipb_halle.server.data.dtos.LinkTypeDTO;
import de.ipb_halle.server.data.interfaces.ILinkTypeRepository;
import de.ipb_halle.server.services.interfaces.ILinkTypeService;
import de.ipb_halle.server.services.interfaces.PageResult;
import de.ipb_halle.server.services.interfaces.QueryCommand;

@Service
public class LinkTypeService implements ILinkTypeService {

    private final ILinkTypeRepository linkTypeRepository;

    @Autowired
    public LinkTypeService(ILinkTypeRepository linkTypeRepository) {
        this.linkTypeRepository = linkTypeRepository;
    }

    @Override
    public PageResult<LinkTypeDTO> GetPage(QueryCommand queryCommand) {
        return linkTypeRepository.GetPage(queryCommand);
    }
}