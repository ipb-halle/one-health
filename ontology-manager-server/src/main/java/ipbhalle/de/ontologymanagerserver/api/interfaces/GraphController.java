package ipbhalle.de.ontologymanagerserver.api.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.EntityDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.LinkDTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IGraphService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IPagedCrudHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.List;

public class GraphController {
    private final IGraphService graphService;

    public GraphController(IGraphService graphService){
        this.graphService = graphService;
    }

    @GetMapping("get-initial")
    public ResponseEntity<GraphDTO> GetInitialGraph() {
       return new ResponseEntity<>(graphService.GetInitialSet(), HttpStatus.OK);
    }


    @PostMapping("get-node-expansion/{id}")
    public ResponseEntity<GraphDTO> GetAdjacentNodes(@PathVariable String id, @RequestBody List<String> nodes) {
      return new ResponseEntity<>(graphService.GetAdjacentNodes(id, nodes), HttpStatus.OK);
    }

    @GetMapping("get-links-between")
    public ResponseEntity<List<LinkDTO>> GetLinksBetween(@RequestParam(name = "sourceId", required = true) String sourceId, @RequestParam(name = "targetId", required = true) String targetId, @RequestParam(name = "type", required = false) String type){
        var result =graphService.GetLinks( sourceId, targetId, type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("get-node/{id}")
    public ResponseEntity<EntityDTO> GetNode(@PathVariable String id) {
        var result = graphService.GetNode(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
