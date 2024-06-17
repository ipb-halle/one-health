package ipbhalle.de.ontologymanagerserver.services;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import ipbhalle.de.ontologymanagerserver.data.dtos.*;
import ipbhalle.de.ontologymanagerserver.data.enums.Cardinality;
import ipbhalle.de.ontologymanagerserver.data.enums.DataType;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntityTypeRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.ILinkTypeRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IOntologyInitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OntologyInitializerService implements IOntologyInitializerService {

    private final IEntityTypeRepository entityTypeRepository;
    private final ILinkTypeRepository linkTypeRepository;

    @Autowired
    public OntologyInitializerService(
            IEntityTypeRepository entityTypeRepository,
            ILinkTypeRepository linkTypeRepository
    )
    {
        this.entityTypeRepository = entityTypeRepository;
        this.linkTypeRepository = linkTypeRepository;
    }

    @Override
    public void EnsureInitialData() {

        var test = entityTypeRepository.GetAll();
        if (!test.isEmpty())
            return;

        PropertyInfoDTO[] plantProperties = {
            new PropertyInfoDTO("plant_kingdom", "Kingdom", "", false, DataType.STRING, false, 0, false),
            new PropertyInfoDTO("plant_phylum", "Phylum", "", false, DataType.STRING, false, 1, false),
            new PropertyInfoDTO("plant_family", "Family", "", false, DataType.STRING, false, 2, false),
            new PropertyInfoDTO("plant_sub_family", "Sub-family", "", false, DataType.STRING, false, 3, false),
            new PropertyInfoDTO("plant_genus", "Genus", "", false, DataType.STRING, false, 4, false),
            new PropertyInfoDTO("plant_species", "Species", "", false, DataType.STRING, false, 5, false),
            new PropertyInfoDTO("plant_sub_species", "Sub-species", "", false, DataType.STRING, false, 6, false),
            new PropertyInfoDTO("plant_scientific_name", "Scientific Name", "", true, DataType.STRING, false, 7, true),
        };

        PropertyInfoDTO[] npProperties = {
            new PropertyInfoDTO("np_inchi", "InChI", "", true, DataType.STRING, false, 1, false),
            new PropertyInfoDTO("np_inchikey", "InChI Key", "", true, DataType.STRING, false, 2, false),
            new PropertyInfoDTO("np_smiles", "SMILES", "", true, DataType.STRING, false, 3, false),
            new PropertyInfoDTO("np_molformula", "Molecular Formula", "", false, DataType.STRING, false, 4, true),
            new PropertyInfoDTO("np_molweight", "Molecular Weight", "", false, DataType.STRING, false, 5, false),
            new PropertyInfoDTO("np_cas", "Cas Registry Number", "", false, DataType.STRING, false, 6, false),
            new PropertyInfoDTO("np_iupac", "IUPAC Name", "", false, DataType.STRING, false, 7, false),

        };

        PropertyInfoDTO[] diseaseProperties = {
                new PropertyInfoDTO("disease_name", "Name", "", true, DataType.STRING, false, 0, true),
                new PropertyInfoDTO("disease_classification", "Classification", "", false, DataType.STRING, false, 1, false)
        };


        EntityTypeDTO[] entityTypes = {
                new EntityTypeDTO(
                        "plant",
                        "Plant",
                        "Plants",
                        null,
                        "A multicellular eukaryotic organism that belongs to the kingdom Plantae.",
                        "#297e00",
                        new HashSet<>(new ArrayList<KeywordDTO>()),
                        new HashSet<PropertyInfoDTO>(List.of(plantProperties)),
                        new HashSet<>(new ArrayList<DataSourceDTO>())

                ),
                new EntityTypeDTO(
                        "np",
                        "Natural Product",
                        "Natural Products",
                        null,
                        "A chemical compound produced by a living organism through its natural biochemical pathways.",
                        "#343ea0",
                        new HashSet<>(new ArrayList<KeywordDTO>()),
                        new HashSet<>(List.of(npProperties)),
                new HashSet<>(new ArrayList<DataSourceDTO>())

                ),
                new EntityTypeDTO(
                        "disease",
                        "Disease",
                        "Diseases",
                        null,
                        "A condition that impairs the normal functioning of the body or one of its parts, and it is typically associated with specific symptoms and signs.",
                        "#b1002a",
                        new HashSet<>(new ArrayList<KeywordDTO>()),
                        new HashSet<>(List.of(diseaseProperties)),
                        new HashSet<>(new ArrayList<DataSourceDTO>())
                )
        };


        for (EntityTypeDTO entityType : entityTypes) {
            entityTypeRepository.Create(entityType);
        }

        LinkTypeDTO[] linkTypes = {
                new LinkTypeDTO(
                        "plant_treats_disease",
                        "TREATS",
                        "OUTGOING",
                        "plant",
                        Cardinality.MANY,
                        "disease",
                        Cardinality.MANY,
                        "The natural product can be used to treat the disease"
                ),
                new LinkTypeDTO(
                        "plant_produces_np",
                        "PRODUCES",
                        "OUTGOING",
                        "plant",
                        Cardinality.MANY,
                        "np",
                        Cardinality.MANY,
                        "The natural product can be obtained from the plant"
                ),
                new LinkTypeDTO(
                        "np_treats_disease",
                        "TREATS",
                        "OUTGOING",
                        "np",
                        Cardinality.MANY,
                        "disease",
                        Cardinality.MANY,
                        "The natural product can be used to treat the disease"
                )
        };

        for (LinkTypeDTO linkType: linkTypes) {
            linkTypeRepository.Create(linkType);
        }

    }
}
