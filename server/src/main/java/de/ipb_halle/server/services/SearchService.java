package de.ipb_halle.server.services;

import org.springframework.beans.factory.annotation.Autowired;

import de.ipb_halle.server.data.interfaces.ISearchRepository;
import de.ipb_halle.server.services.interfaces.ISearchService;

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
