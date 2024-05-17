package ipbhalle.de.ontologymanagerserver.api.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IGraphService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IPagedCrudHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
      return new ResponseEntity<>(graphService.GetAdjacentNodes(id), HttpStatus.OK);
    }

}
