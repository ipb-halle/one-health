package ipbhalle.de.ontologymanagerserver.api;

import ipbhalle.de.ontologymanagerserver.api.interfaces.GraphController;
import ipbhalle.de.ontologymanagerserver.data.dtos.CoOcurrenceQuery;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IEntityService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IOntologyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/entity")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class EntityController extends GraphController {

    private IEntityService entityService;

    public EntityController(IEntityService entityService) {
        super(entityService);
        this.entityService = entityService;
    }

}
