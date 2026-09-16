/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

import de.ipb_halle.curator.fields.FieldService;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.onehealth.ElementService;
import de.ipb_halle.curator.source.coconut.CoconutImportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author fblocal
 */
@Component
public class ImportHandlerFactory {

    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private ElementService elementService;

    @Autowired
    private FieldService fieldService;


    public ImportHandler build() {
        return new CoconutImportHandler()
                .setElementService(elementService)
                .setFieldService(fieldService)
                .setMetadataRegistry(registry);
    }
}
