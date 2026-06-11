package de.ipb_halle.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ipb_halle.server.data.dtos.NaturalProductDTO;
import de.ipb_halle.server.data.interfaces.IEntityRepository;
import de.ipb_halle.server.data.interfaces.INaturalProductRepository;
import de.ipb_halle.server.services.interfaces.INaturalProductService;
import de.ipb_halle.server.utils.security.StringProcessing;

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
        if (StringProcessing.isSQLInjection(value)) {
            throw new RuntimeException();
        }

        return this.naturalProductRepository.GetBySMILES(value);
    }

    @Override
    public NaturalProductDTO GetByInChI(String value) {
        if (StringProcessing.isSQLInjection(value)) {
            throw new RuntimeException();
        }
        return this.naturalProductRepository.GetByInChI(value);
    }

    @Override
    public NaturalProductDTO GetByInChIKey(String value) {
        if (StringProcessing.isSQLInjection(value)) {
            throw new RuntimeException();
        }
        return this.naturalProductRepository.GetByInChIKey(value);
    }

    @Override
    public List<NaturalProductDTO> GetBySubstructure(String smiles, int take, int page) {
        if (StringProcessing.isSQLInjection(smiles)) {
            throw new RuntimeException();
        }
        page = 0;
        take = Math.min(take, 1000);
        return this.naturalProductRepository.GetBySubstructure(smiles, take, page);
    }

    @Override
    public List<NaturalProductDTO> GetBySimilarity(String smiles, int threshold, int limit){
        if (StringProcessing.isSQLInjection(smiles)) {
            throw new RuntimeException();
        }
        if (threshold < 0 || threshold > 100)
            threshold = 80;
        limit = Math.min(limit, 1000);
        return this.naturalProductRepository.GetBySimilarity(smiles, threshold, limit);
    }
}
