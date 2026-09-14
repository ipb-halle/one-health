package de.ipb_halle.server.api;

import de.ipb_halle.server.services.interfaces.IDataSourceService;

/**
 * Legacy data-source REST endpoints were not used by the active frontend and were
 * never registered as active controllers. This class is deliberately kept as a
 * non-controller stub to avoid exposing dead routes.
 */
public class DataSourceController {

    private final IDataSourceService dataSourceService;

    public DataSourceController(IDataSourceService crudHandler) {
        this.dataSourceService = crudHandler;
    }
}
