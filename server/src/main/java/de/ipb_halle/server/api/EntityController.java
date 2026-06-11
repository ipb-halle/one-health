package de.ipb_halle.server.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.ipb_halle.server.api.interfaces.GraphController;
import de.ipb_halle.server.data.dtos.LinkDTO;
import de.ipb_halle.server.services.interfaces.IEntityService;

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

    @PostMapping("get-graph-references")
    public ResponseEntity<List<LinkDTO>> GetGraphReferences(@RequestBody List<String> nodes) {
        return new ResponseEntity<>(entityService.GetGraphReferences(nodes), HttpStatus.OK);
    }

}
