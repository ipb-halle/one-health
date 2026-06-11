package de.ipb_halle.server.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.ipb_halle.server.data.dtos.GraphDTO;
import de.ipb_halle.server.data.dtos.MetadataElementDTO;
import de.ipb_halle.server.data.dtos.MetadataSummaryDTO;
import de.ipb_halle.server.data.dtos.SelectableOption;
import de.ipb_halle.server.services.interfaces.IMetadataService;

import java.util.List;

@RestController
@RequestMapping(path="api/metadata")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class MetadataController {


    IMetadataService metadataService;

    public MetadataController(IMetadataService metadataService){
        this.metadataService = metadataService;
    }

    @GetMapping("all")
    public ResponseEntity<GraphDTO>  GetAll(){
        return new ResponseEntity<>(metadataService.GetAll(), HttpStatus.OK);

    }

    @GetMapping("as-options")
    public ResponseEntity<List<SelectableOption<String>>> GetAllAsOptions() {
        return new ResponseEntity<>(metadataService.GetAllAsOptions(), HttpStatus.OK);
    }

    @GetMapping("entity-type/{id}")
    public ResponseEntity<MetadataElementDTO> GetEntityType(@PathVariable String id){
        return new ResponseEntity<>(metadataService.GetEntityType(id), HttpStatus.OK);
    }

    @GetMapping("link-type/{id}")
    public ResponseEntity<MetadataElementDTO> GetLinkType(@PathVariable String id){
        return new ResponseEntity<>(metadataService.GetLinkType(id), HttpStatus.OK);
    }

    @GetMapping("summary")
    public ResponseEntity<MetadataSummaryDTO> GetSummary()
    {
        return new ResponseEntity<>(metadataService.GetSummary(), HttpStatus.OK);
    }
}
