package ipbhalle.de.ontologymanagerserver.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="api/ontology")
@CrossOrigin(origins = "*") // TODO: make this inheritable ? and use an env variable
public class NaturalProductController {
}
