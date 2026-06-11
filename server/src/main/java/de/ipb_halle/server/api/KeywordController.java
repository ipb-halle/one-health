package de.ipb_halle.server.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ipb_halle.server.api.interfaces.DataController;
import de.ipb_halle.server.data.dtos.KeywordDTO;
import de.ipb_halle.server.services.interfaces.IKeywordService;

@RestController
@RequestMapping(path="api/keyword")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class KeywordController extends DataController<KeywordDTO, String>  {
    public KeywordController(IKeywordService keywordService) {
        super(keywordService);
    }
}
