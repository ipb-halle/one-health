package de.ipb_halle.server.api.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.ipb_halle.server.data.dtos.EntityDTO;
import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.LinkDTO;
import de.ipb_halle.server.services.interfaces.IGraphService;

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
        var result = graphService.GetNodes(List.of(id));
        return new ResponseEntity<>(result.isEmpty() ? null : result.get(0), HttpStatus.OK);
    }


}
