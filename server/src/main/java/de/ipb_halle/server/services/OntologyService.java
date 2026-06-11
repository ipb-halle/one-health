package de.ipb_halle.server.services;

import de.ipb_halle.server.data.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ipb_halle.server.data.dtos.*;
import de.ipb_halle.server.data.interfaces.IOntologyRepository;
import de.ipb_halle.server.services.interfaces.IOntologyService;

import java.util.List;

@Service
public class OntologyService implements IOntologyService {

    private IOntologyRepository ontologyRepository;

    @Autowired
    public OntologyService(IOntologyRepository ontologyRepository) {
        this.ontologyRepository = ontologyRepository;
    }

    @Override
    public List<EntityDTO> GetAsGraph() {
        return ontologyRepository.GetAsGraph();
    }

    @Override
    public GraphDTO FindCoOccurrences(CoOcurrenceQuery query) {
        return ontologyRepository.FindCoOccurrences(query);
    }

    @Override
    public GraphDTO GetInitialSet() {
        return ontologyRepository.GetInitialSet();
    }

    @Override
    public GraphDTO GetAdjacentNodes(String nodeId, List<String> nodes) {
        return ontologyRepository.GetAdjacentNodes(nodeId, nodes);
    }

    @Override
    public List<EntityDTO> GetNodes(List<String> nodeIds) {
        return null;
    }

    @Override
    public GraphLinkDTO GetLink(String nodeId) {
        return null;
    }

    @Override
    public List<LinkDTO> GetLinks(String sourceId, String targetId, String type) {
        return List.of();
    }

    @Override
    public List<LinkDTO> FindCoOccurrencesDetails(CoOcurrenceQuery query){
        return ontologyRepository.FindCoOccurrencesDetails(query);
    }
}
