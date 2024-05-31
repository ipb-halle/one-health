package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.SelectableOption;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityTypeRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IEntityTypeService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.PageResult;
import ipbhalle.de.ontologymanagerserver.services.interfaces.QueryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityTypeService implements IEntityTypeService {

    private final IEntityTypeRepository entityRepository;

    @Autowired
    public EntityTypeService(IEntityTypeRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public EntityTypeDTO Create(EntityTypeDTO dto) {
        return entityRepository.Create(dto);
    }

    @Override
    public void Delete(String id) {
        entityRepository.Delete(id);
    }

    @Override
    public EntityTypeDTO Get(String id) {

        return entityRepository.Get(id);
    }

    @Override
    public List<EntityTypeDTO> GetAll() {

        return entityRepository.GetAll();
    }

    @Override
    public PageResult<EntityTypeDTO> GetPage(QueryCommand queryCommand) {
        var items = entityRepository.GetAll();
        return new PageResult<>(items.size(), items);
    }


    @Override
    public EntityTypeDTO Update(EntityTypeDTO dto) {
        return entityRepository.Update(dto);
    }

    @Override
    public List<SelectableOption<String>> getEntityTypeAsOptions() {
        return entityRepository.getEntityTypeAsOptions();
    }
}
