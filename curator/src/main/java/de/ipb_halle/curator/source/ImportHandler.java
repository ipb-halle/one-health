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
import java.io.IOException;

/**
 *
 * @author fblocal
 */
public interface ImportHandler {

    public ImportHandler setElementService(ElementService elementService);
    public ImportHandler setFieldConverter(FieldConverter converter);
    public ImportHandler setFieldService(FieldService fieldService);
    public ImportHandler setMetadataRegistry(MetadataRegistry registry);

    public void importSource(DataSource source) throws IOException;

}
