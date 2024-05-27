package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.NaturalProductDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.INaturalProductRepository;
import ipbhalle.de.ontologymanagerserver.data.query.NaturalProductStructureQuery;
import ipbhalle.de.ontologymanagerserver.services.interfaces.INaturalProductService;
import org.hibernate.NaturalIdLoadAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class NaturalProductService implements INaturalProductService {

    private final INaturalProductRepository naturalProductRepository;
    private final IEntityRepository entityRepository;

    @Autowired
    public NaturalProductService(INaturalProductRepository naturalProductRepository, IEntityRepository entityRepository){
        this.naturalProductRepository = naturalProductRepository;
        this.entityRepository = entityRepository;
    }

    @Override
    public NaturalProductDTO GetBySMILES(String value) {
        var result =  this.naturalProductRepository.GetBySMILES(value);
        if (result == null)
            return null;
        var ids = new String[] { result.getId()} ;
        return this.entityRepository.GetIds(Arrays.stream(ids).toList()).get(0);
    }

    @Override
    public NaturalProductDTO GetByInChI(String value) {
        var result = this.naturalProductRepository.GetByInChI(value);
        if (result == null)
            return null;
        var ids = new String[] { result.getId()} ;
        return this.entityRepository.GetIds(Arrays.stream(ids).toList()).get(0);
    }

    @Override
    public NaturalProductDTO GetByInChIKey(String value) {
        var result = this.naturalProductRepository.GetByInChIKey(value);
        if (result == null)
            return null;
        var ids = new String[] { result.getId()} ;
        return this.entityRepository.GetIds(Arrays.stream(ids).toList()).get(0);
    }

    @Override
    public List<NaturalProductDTO> GetBySubstructure(String smiles, int take, int page) {
        var result = this.naturalProductRepository.GetBySubstructure(smiles, take, page);
        var ids = result.stream().map(NaturalProductDTO::getId);
        return this.entityRepository.GetIds(ids.toList());
    }

    @Override
    public List<NaturalProductDTO> GetBySimilarity(String smiles, int threshold, int limit){
        var result = this.naturalProductRepository.GetBySimilarity(smiles, threshold, limit);
        var ids = result.stream().map(NaturalProductDTO::getId);
        return this.entityRepository.GetIds(ids.toList());
    }
}
