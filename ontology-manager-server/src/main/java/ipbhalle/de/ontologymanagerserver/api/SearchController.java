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
@RequestMapping(path="api/search")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class SearchController {


    public SearchController() {
    }


    @GetMapping("{query}")
    public List<EntityDTO> SearchTerm(@PathVariable String query){
        return null;
    }


}


