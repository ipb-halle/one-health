package ipbhalle.de.ontologymanagerserver.data.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.NaturalProductDTO;
import ipbhalle.de.ontologymanagerserver.data.query.NaturalProductStructureQuery;

import java.util.List;

public interface INaturalProductRepository {
    NaturalProductDTO GetBySMILES(String value);
    NaturalProductDTO GetByInChI(String value);
    NaturalProductDTO GetByInChIKey(String value);
    List<NaturalProductDTO> GetBySubstructure(String smiles, int take, int page);
    List<NaturalProductDTO> GetBySimilarity(String smiles, int threshold, int limit);

}
