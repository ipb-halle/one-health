package de.ipb_halle.server.api.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import de.ipb_halle.server.data.interfaces.DTO;
import de.ipb_halle.server.services.interfaces.ICrudHandler;

/**
 * Opt-in CRUD base for controllers that intentionally expose creation.
 */
public abstract class CreatableDataController<TDTO extends DTO<TKey>, TKey extends Comparable<TKey>>
        extends DataController<TDTO, TKey> {

    protected CreatableDataController(ICrudHandler<TDTO, TKey> crudHandler) {
        super(crudHandler);
    }

    @PostMapping
    public ResponseEntity<TDTO> Create(@RequestBody TDTO dto) {
        TDTO result = crudHandler.Create(dto);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.CREATED);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}