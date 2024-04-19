package ipbhalle.de.ontologymanagerserver.api;

import ipbhalle.de.ontologymanagerserver.api.interfaces.DataController;
import ipbhalle.de.ontologymanagerserver.api.interfaces.PagedDataController;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.SelectableOption;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IEntityTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/entity-type")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class EntityTypeController extends PagedDataController<EntityTypeDTO, EntityTypeDTO, String> {

    private final IEntityTypeService entityTypeService;
    public EntityTypeController(IEntityTypeService crudHandler) {

        super(crudHandler);
        this.entityTypeService = crudHandler;
    }

    @GetMapping("as-options")
    public List<SelectableOption<String>> getAllEntityTypesAsOptions() {
        return entityTypeService.getEntityTypeAsOptions();
    }

}
