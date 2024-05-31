package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntitySearchResultDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntitySearchRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityTypeRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IEntitySearchService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IEntityService;
import ipbhalle.de.ontologymanagerserver.utils.security.StringProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntitySearchService implements IEntitySearchService {



    private final IEntitySearchRepository entitySearchRepository;
    private final IEntityRepository entityRepository;


    @Autowired
    public EntitySearchService(IEntitySearchRepository entitySearchRepository, IEntityRepository entityRepository) {
        this.entitySearchRepository = entitySearchRepository;
        this.entityRepository = entityRepository;
    }

    @Override
    public List<EntitySearchResultDTO> FindEntities(String query) {
        if (StringProcessing.isSQLInjection(query)) {
            throw new RuntimeException();
        }
        var ids = entitySearchRepository.FindMatchingEntityIds(query);
        return entityRepository.GetByIds(ids);
    }
}
