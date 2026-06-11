package de.ipb_halle.server.data.interfaces;

import java.util.List;

import de.ipb_halle.server.data.dtos.NaturalProductDTO;

public interface INaturalProductRepository {
    NaturalProductDTO GetBySMILES(String value);
    NaturalProductDTO GetByInChI(String value);
    NaturalProductDTO GetByInChIKey(String value);
    List<NaturalProductDTO> GetBySubstructure(String smiles, int take, int page);
    List<NaturalProductDTO> GetBySimilarity(String smiles, int threshold, int limit);

}
