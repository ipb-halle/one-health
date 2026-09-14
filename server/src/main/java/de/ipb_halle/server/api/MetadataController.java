package de.ipb_halle.server.api;

import de.ipb_halle.server.services.interfaces.IMetadataService;

/**
 * Legacy metadata endpoints were not used by the active frontend and are no
 * longer exposed as Spring REST routes.
 */
public class MetadataController {
    private final IMetadataService metadataService;

    public MetadataController(IMetadataService metadataService) {
        this.metadataService = metadataService;
    }
}
