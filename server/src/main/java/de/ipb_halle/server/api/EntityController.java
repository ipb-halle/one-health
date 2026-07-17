package de.ipb_halle.server.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.ipb_halle.server.api.interfaces.GraphController;
import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.LinkDTO;
import de.ipb_halle.server.data.interfaces.IEntityRepository;
import de.ipb_halle.server.services.interfaces.IEntityService;
import java.util.ArrayList;

import java.util.List;

@RestController
@RequestMapping(path = "api/entity")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class EntityController extends GraphController {

    private IEntityService entityService;
    private IEntityRepository entityRepository;

    public EntityController(
            IEntityService entityService,
            IEntityRepository entityRepository
    ) {
        super(entityService);
        this.entityService = entityService;
        this.entityRepository = entityRepository;
    }

    @PostMapping("get-graph-references")
    public ResponseEntity<List<LinkDTO>> GetGraphReferences(@RequestBody List<String> nodes) {
        return new ResponseEntity<>(entityService.GetGraphReferences(nodes), HttpStatus.OK);
    }

    @GetMapping("getAdjacentEntities")
    public ResponseEntity<List<EntityDTO>> getAdjacentEntities(@RequestParam String id) {

        GraphDTO graphDto = entityService.GetAdjacentNodes(id, new ArrayList<>());

        List<String> nodeIds = graphDto.getNodes().stream().map(node -> node.getId()).toList();

        List<EntityDTO> entities = entityRepository.GetNodes(nodeIds);

        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

}
