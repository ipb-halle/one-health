package de.ipb_halle.server.services;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.interfaces.IEntityRepository;
import de.ipb_halle.server.data.interfaces.IEntitySearchRepository;
import de.ipb_halle.server.services.interfaces.IEntitySearchService;
import de.ipb_halle.server.utils.security.StringProcessing;

import java.util.*;

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
    public List<EntityDTO> FindEntities(String query) {

        if (StringProcessing.isSQLInjection(query)) {
            throw new RuntimeException();
        }
        List<String> ids = new ArrayList<>(entitySearchRepository.FindMatchingEntityIds(query));

            SmilesParser parser = new SmilesParser(DefaultChemObjectBuilder.getInstance());

            try {
                IAtomContainer molecule = parser.parseSmiles(query);

                InChIGenerator generator = InChIGeneratorFactory.getInstance().getInChIGenerator(molecule);

                String inchi = generator.getInchi();
                ids.addAll(entitySearchRepository.FindMatchingEntityIds(inchi));
            }

            catch(CDKException ignored){}

        return entityRepository.GetNodes(ids.stream().distinct().toList());

    }

}



