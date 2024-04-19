package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.LinkTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.SelectableOption;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityTypeRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.ILinkTypeRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IEntityTypeService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.ILinkTypeService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.PageResult;
import ipbhalle.de.ontologymanagerserver.services.interfaces.QueryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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