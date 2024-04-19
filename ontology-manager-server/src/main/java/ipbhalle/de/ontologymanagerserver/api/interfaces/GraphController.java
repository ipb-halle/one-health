package ipbhalle.de.ontologymanagerserver.api.interfaces;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IGraphService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IPagedCrudHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class GraphController {
    private final IGraphService graphService;

    public GraphController(IGraphService graphService){
        this.graphService = graphService;
    }

    @GetMapping("initial-graph")
    public ResponseEntity<GraphDTO> GetInitialGraph() {
       return new ResponseEntity<>(graphService.GetInitialSet(), HttpStatus.OK);
    }


    @GetMapping("adjacent-nodes")
    public ResponseEntity<GraphDTO> GetAdjacentNodes(String nodeId) {
        return new ResponseEntity<>(graphService.GetAdjacentNodes(nodeId), HttpStatus.OK);
    }

}
