package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IOntologyRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IOntologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public EntityDTO GetNode(String nodeId) {
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
