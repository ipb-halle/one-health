package ipbhalle.de.ontologymanagerserver.api.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TypeKey;
import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.ICrudHandler;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IPagedCrudHandler;
import ipbhalle.de.ontologymanagerserver.services.interfaces.PageResult;
import ipbhalle.de.ontologymanagerserver.services.interfaces.QueryCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;


public abstract class PagedDataController<TDTO extends DTO<TKey>, TPageDTO extends DTO<TKey>, TKey extends Comparable<TKey> > extends DataController<TDTO, TKey>{

    private final IPagedCrudHandler<TDTO, TPageDTO, TKey> crudHandler;

    public PagedDataController(IPagedCrudHandler<TDTO, TPageDTO, TKey> crudHandler) {
        super(crudHandler);
        this.crudHandler = crudHandler;
    }

    @GetMapping("getPage")
    public ResponseEntity<PageResult<TDTO>> Get(@ModelAttribute QueryCommand query){

        PageResult<TDTO> result = crudHandler.GetPage(new QueryCommand());
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

}
