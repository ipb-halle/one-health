package de.ipb_halle.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import de.ipb_halle.server.data.dtos.*;
import de.ipb_halle.server.services.interfaces.IEntitySearchService;

import java.util.List;

@RestController
@RequestMapping(path="api/search")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class SearchController {

    private final IEntitySearchService searchService;

    @Autowired
    public SearchController(IEntitySearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping()
    public List<EntityDTO> SearchTerm(@RequestParam String query) {

        return searchService.FindEntities(query);

    }


}