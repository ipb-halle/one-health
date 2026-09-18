/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

import de.ipb_halle.curator.fields.FieldConverter;
import de.ipb_halle.curator.fields.FieldService;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.onehealth.ElementService;
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
    private FieldConverter fieldConverter;

    @Autowired
    private FieldService fieldService;


    public ImportHandler build(Class handlerClass) {
        try {
            ImportHandler handler = (ImportHandler) handlerClass
                    .getConstructor(new Class[0])
                    .newInstance();

            return handler
                    .setElementService(elementService)
                    .setFieldConverter(fieldConverter)
                    .setFieldService(fieldService)
                    .setMetadataRegistry(registry);
        } catch (Exception ex) {
            throw new RuntimeException("Instantiation of ImportHandler failed.", ex);
        }
    }
}
