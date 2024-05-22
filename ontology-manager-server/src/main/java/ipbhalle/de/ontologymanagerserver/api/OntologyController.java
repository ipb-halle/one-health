package ipbhalle.de.ontologymanagerserver.api;

import ipbhalle.de.ontologymanagerserver.api.interfaces.GraphController;
import ipbhalle.de.ontologymanagerserver.data.dtos.CoOcurrenceQuery;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.LinkDTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IOntologyService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping(path="api/ontology")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class OntologyController {

    private IOntologyService ontologyService;

    public OntologyController(IOntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }


    @GetMapping("as-graph")
    public List<EntityDTO> GetAsGraph(){
        return ontologyService.GetAsGraph();
    }

    @GetMapping("find-co-ocurrences")
    public GraphDTO FindCoOccurrences(@ModelAttribute CoOcurrenceQuery query)
    {
        return ontologyService.FindCoOccurrences(query);
    }

    @GetMapping("find-co-occurrences-details")
    public List<LinkDTO> FindCoOccurrencesDetails(@ModelAttribute CoOcurrenceQuery query)
    {
        return ontologyService.FindCoOccurrencesDetails(query);
    }


}
