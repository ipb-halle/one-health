package ipbhalle.de.ontologymanagerserver.api.interfaces;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.ICrudHandler;
import ipbhalle.de.ontologymanagerserver.services.interfaces.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public abstract class DataController<TDTO extends DTO<TKey>, TKey extends Comparable<TKey>> {

    private final ICrudHandler<TDTO, TKey> crudHandler;

    public DataController(ICrudHandler<TDTO, TKey> crudHandler) {
        this.crudHandler = crudHandler;
    }

    @PostMapping()
    public ResponseEntity<TDTO> Create(@RequestBody TDTO dto){
        TDTO result = crudHandler.Create(dto);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.CREATED);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TDTO> Update(@RequestBody TDTO dto){
        TDTO result = crudHandler.Update(dto);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.CREATED);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("{id}")
    public ResponseEntity<TDTO> Get(@PathVariable TKey id) {
        TDTO result = crudHandler.Get(id);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public void Delete(@PathVariable TKey id){
        crudHandler.Delete(id);
    }


    @GetMapping("all")
    public ResponseEntity<List<TDTO>> Get(){
        List<TDTO> result = crudHandler.GetAll();
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }


}
