/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator;

import de.ipb_halle.curator.fields.FieldConverter;
import de.ipb_halle.curator.fields.FieldService;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.onehealth.ElementService;
import de.ipb_halle.curator.onehealth.NodeWriter;
import de.ipb_halle.curator.source.DataSource;
import de.ipb_halle.curator.source.ImportHandler;
import de.ipb_halle.curator.source.SourceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Phase 1: Import and Export
 */
@Component
@Order(1)
public class CuratorCliRunner implements CommandLineRunner {

    @Autowired
    private MetadataRegistry metadataRegistry;

    @Autowired
    private SourceRegistry sourceRegistry;

    @Autowired
    private ElementService elementService;

    @Autowired
    private FieldConverter fieldConverter;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private NodeWriter nodeWriter;

    @Override
    public void run(String... args) throws Exception {
        for (DataSource source : sourceRegistry.getDataSources()) {
            ImportHandler handler = (ImportHandler) source.getHandler().
                    getConstructor(new Class[0])
                    .newInstance();
            handler.setElementService(elementService);
            handler.setFieldConverter(fieldConverter);
            handler.setFieldService(fieldService);
            handler.setMetadataRegistry(metadataRegistry);
            handler.importSource(source);
        }
        // nodeWriter.writeNodes(elementType, output);
    }
}
