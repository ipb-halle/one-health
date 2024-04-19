package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.MetadataElementDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.MetadataSummaryDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.SelectableOption;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IKeywordRepository;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IMetadataRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MetadataService implements IMetadataService {

    private final IMetadataRepository metadataRepository;

    @Autowired
    public MetadataService(IMetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    @Override
    public GraphDTO GetAll() {
        return this.metadataRepository.GetAll();
    }

    @Override
    public MetadataElementDTO GetEntityType(String id) {
        return this.metadataRepository.GetEntityType(id);
    }

    @Override
    public MetadataElementDTO GetLinkType(String id) {
        return this.metadataRepository.GetLinkType(id);
    }

    @Override
    public MetadataSummaryDTO GetSummary() {
        return this.metadataRepository.GetSummary();
    }

    @Override
    public List<SelectableOption<String>> GetAllAsOptions() {
        return metadataRepository.getAllAsOptions();
    }


}
