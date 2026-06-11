package de.ipb_halle.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ipb_halle.server.data.dtos.LinkTypeDTO;
import de.ipb_halle.server.data.interfaces.ILinkTypeRepository;
import de.ipb_halle.server.services.interfaces.ILinkTypeService;
import de.ipb_halle.server.services.interfaces.PageResult;
import de.ipb_halle.server.services.interfaces.QueryCommand;

import java.util.List;

@Service
public class LinkTypeService implements ILinkTypeService {

    private final ILinkTypeRepository linkTypeRepository;

    @Autowired
    public LinkTypeService(ILinkTypeRepository linkTypeRepository) {
        this.linkTypeRepository = linkTypeRepository;
    }


    @Override
    public LinkTypeDTO Create(LinkTypeDTO dto) {
        return linkTypeRepository.Create(dto);
    }

    @Override
    public LinkTypeDTO Update(LinkTypeDTO dto) {
        return null;
    }

    @Override
    public LinkTypeDTO Get(String id) {
        return linkTypeRepository.Get(id);
    }

    @Override
    public List<LinkTypeDTO> GetAll() {
        return null;
    }

    @Override
    public void Delete(String id) {

    }

    @Override
    public PageResult<LinkTypeDTO> GetPage(QueryCommand queryCommand) {

        var result = linkTypeRepository.GetAll();
        return new PageResult<>(result.size(), result);
    }
}