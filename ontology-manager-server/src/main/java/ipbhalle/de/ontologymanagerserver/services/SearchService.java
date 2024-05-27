package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.interfaces.ISearchRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchService implements ISearchService {

    private final ISearchRepository searchRepository;

    @Autowired
    SearchService(ISearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }


    @Override
    public void FindAll(String query) {
    }
}
