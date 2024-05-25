package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.NaturalProductDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.INaturalProductRepository;
import ipbhalle.de.ontologymanagerserver.data.query.NaturalProductStructureQuery;
import ipbhalle.de.ontologymanagerserver.services.interfaces.INaturalProductService;
import org.hibernate.NaturalIdLoadAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NaturalProductService implements INaturalProductService {

    private final INaturalProductRepository naturalProductRepository;

    @Autowired
    public NaturalProductService(INaturalProductRepository naturalProductRepository){
        this.naturalProductRepository = naturalProductRepository;
    }

    @Override
    public NaturalProductDTO GetBySMILES(String value) {
        return this.naturalProductRepository.GetBySMILES(value);
    }

    @Override
    public NaturalProductDTO GetByInChI(String value) {
        return this.naturalProductRepository.GetByInChI(value);
    }

    @Override
    public NaturalProductDTO GetByInChIKey(String value) {
        return this.naturalProductRepository.GetByInChIKey(value);
    }

    @Override
    public List<NaturalProductDTO> GetBySubstructure(String smiles, int take, int page) {
        return this.naturalProductRepository.GetBySubstructure(smiles, take, page);
    }

    @Override
    public List<NaturalProductDTO> GetBySimilarity(String smiles, int threshold, int limit){
        return this.naturalProductRepository.GetBySimilarity(smiles, threshold, limit);
    }
}
