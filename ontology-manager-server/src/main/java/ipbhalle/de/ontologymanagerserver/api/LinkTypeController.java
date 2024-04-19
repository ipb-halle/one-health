package ipbhalle.de.ontologymanagerserver.api;

import ipbhalle.de.ontologymanagerserver.api.interfaces.DataController;
import ipbhalle.de.ontologymanagerserver.api.interfaces.PagedDataController;
import ipbhalle.de.ontologymanagerserver.data.dtos.LinkTypeDTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IEntityTypeService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.ILinkTypeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
