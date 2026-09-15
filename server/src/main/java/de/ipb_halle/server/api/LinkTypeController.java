package de.ipb_halle.server.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ipb_halle.server.data.dtos.LinkTypeDTO;
import de.ipb_halle.server.services.interfaces.ILinkTypeService;
import de.ipb_halle.server.services.interfaces.PageResult;
import de.ipb_halle.server.services.interfaces.QueryCommand;

@RestController
@RequestMapping(path="api/link-type")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class LinkTypeController {

    private final ILinkTypeService linkTypeService;

    public LinkTypeController(ILinkTypeService linkTypeService) {
        this.linkTypeService = linkTypeService;
    }

    @GetMapping("getPage")
    public ResponseEntity<PageResult<LinkTypeDTO>> getPage(@ModelAttribute QueryCommand query) {
        PageResult<LinkTypeDTO> result = linkTypeService.GetPage(query);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
