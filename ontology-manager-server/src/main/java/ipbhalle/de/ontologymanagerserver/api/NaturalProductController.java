package ipbhalle.de.ontologymanagerserver.api;

import ipbhalle.de.ontologymanagerserver.data.dtos.GraphDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.NaturalProductDTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.INaturalProductService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/compounds")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class NaturalProductController {

    private final INaturalProductService naturalProductService;

    public NaturalProductController(INaturalProductService naturalProductService){
        this.naturalProductService = naturalProductService;
    }

    @GetMapping("by-smiles")
    public ResponseEntity<NaturalProductDTO> GetBySMILES(@RequestParam String value){
        return new ResponseEntity<>(naturalProductService.GetBySMILES(value), HttpStatus.OK);

    }

    @GetMapping("by-inchi")
    public ResponseEntity<NaturalProductDTO> GetByInChI(@RequestParam String value){
        return new ResponseEntity<>(naturalProductService.GetByInChI(value), HttpStatus.OK);

    }

    @GetMapping("by-inchikey")
    public ResponseEntity<NaturalProductDTO> GetByInChIKey(@RequestParam String value){
        return new ResponseEntity<>(naturalProductService.GetByInChIKey(value), HttpStatus.OK);
    }

    @GetMapping("by-substructure")
    public ResponseEntity<PageResult<NaturalProductDTO>> GetBySubstructure(@RequestParam String smiles, @RequestParam int take, @RequestParam int page){
        var results = naturalProductService.GetBySubstructure(smiles, take, page);
        var pageResults = new PageResult<>(results.size(), results);
        return new ResponseEntity<>(pageResults, HttpStatus.OK);
    }

    @GetMapping("by-similarity")
    public ResponseEntity<PageResult<NaturalProductDTO>> GetBySimilarity(@RequestParam String smiles, @RequestParam int threshold, @RequestParam int limit){
        var results = naturalProductService.GetBySimilarity(smiles, threshold, limit);
        var pageResults = new PageResult<>(results.size(), results);
        return new ResponseEntity<>(pageResults, HttpStatus.OK);
    }
}
