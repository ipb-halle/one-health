package de.ipb_halle.server.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ipb_halle.server.api.interfaces.PagedDataController;
import de.ipb_halle.server.data.dtos.LinkTypeDTO;
import de.ipb_halle.server.services.interfaces.ILinkTypeService;

@RestController
@RequestMapping(path="api/link-type")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class LinkTypeController extends PagedDataController<LinkTypeDTO, LinkTypeDTO, String> {

    private final ILinkTypeService linkTypeService;
    public LinkTypeController(ILinkTypeService crudHandler) {

        super(crudHandler);
        this.linkTypeService = crudHandler;
    }



}
