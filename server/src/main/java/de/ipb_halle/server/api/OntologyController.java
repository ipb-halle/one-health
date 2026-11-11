package de.ipb_halle.server.api;

import org.springframework.web.bind.annotation.*;

import de.ipb_halle.server.data.dtos.CoOcurrenceQuery;
import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.LinkDTO;
import de.ipb_halle.server.services.interfaces.IOntologyService;

import java.util.List;

@RestController
@RequestMapping(path = "api/ontology")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class OntologyController {

    private IOntologyService ontologyService;

    public OntologyController(IOntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    @GetMapping("find-co-ocurrences")
    public GraphDTO FindCoOccurrences(@ModelAttribute CoOcurrenceQuery query) {
        return ontologyService.FindCoOccurrences(query);
    }

    @GetMapping("find-co-occurrences-details")
    public List<LinkDTO> FindCoOccurrencesDetails(@ModelAttribute CoOcurrenceQuery query) {
        return ontologyService.FindCoOccurrencesDetails(query);
    }

}
