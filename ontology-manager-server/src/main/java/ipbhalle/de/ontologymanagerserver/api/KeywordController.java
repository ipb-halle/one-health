package ipbhalle.de.ontologymanagerserver.api;

import ipbhalle.de.ontologymanagerserver.api.interfaces.DataController;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.KeywordDTO;
import ipbhalle.de.ontologymanagerserver.services.interfaces.ICrudHandler;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IKeywordService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="api/keyword")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class KeywordController extends DataController<KeywordDTO, String>  {
    public KeywordController(IKeywordService keywordService) {
        super(keywordService);
    }
}
