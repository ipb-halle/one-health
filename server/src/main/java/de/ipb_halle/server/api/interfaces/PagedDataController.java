package de.ipb_halle.server.api.interfaces;

import de.ipb_halle.server.data.interfaces.DTO;
import de.ipb_halle.server.services.interfaces.IPagedCrudHandler;
import de.ipb_halle.server.services.interfaces.PageResult;
import de.ipb_halle.server.services.interfaces.QueryCommand;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
