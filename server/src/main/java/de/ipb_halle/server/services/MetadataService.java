package de.ipb_halle.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.MetadataElementDTO;
import de.ipb_halle.server.data.dtos.MetadataSummaryDTO;
import de.ipb_halle.server.data.dtos.SelectableOption;
import de.ipb_halle.server.data.interfaces.IMetadataRepository;
import de.ipb_halle.server.services.interfaces.IMetadataService;

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
