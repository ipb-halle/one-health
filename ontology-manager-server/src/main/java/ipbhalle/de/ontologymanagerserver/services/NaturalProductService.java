package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.NaturalProductDTO;
import ipbhalle.de.ontologymanagerserver.data.query.NaturalProductStructureQuery;
import ipbhalle.de.ontologymanagerserver.services.interfaces.INaturalProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NaturalProductService implements INaturalProductService {



    @Override
    public NaturalProductDTO Get(String id) {
        return null;
    }

    @Override
    public List<NaturalProductDTO> GetByStructure(NaturalProductStructureQuery query) {
        return List.of();
    }

    @Override
    public List<NaturalProductDTO> GetByProperties() {
        return List.of();
    }
}
