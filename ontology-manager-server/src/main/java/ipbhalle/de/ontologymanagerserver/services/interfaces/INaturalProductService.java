package ipbhalle.de.ontologymanagerserver.services.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.NaturalProductDTO;
import ipbhalle.de.ontologymanagerserver.data.query.NaturalProductStructureQuery;

import java.util.List;

public interface INaturalProductService {
    NaturalProductDTO Get(String id);
    List<NaturalProductDTO> GetByStructure(NaturalProductStructureQuery query);
    List<NaturalProductDTO> GetByProperties();
}
