/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source.coconut;

import de.ipb_halle.curator.fields.FieldService;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.onehealth.ElementService;
import de.ipb_halle.curator.source.DataSource;
import de.ipb_halle.curator.source.FieldMapping;
import de.ipb_halle.curator.source.ImportHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author fblocal
 */
public class CoconutImportHandler implements ImportHandler {

    private ElementService elementService;
    private FieldService fieldService;
    private MetadataRegistry registry;

    private List<FieldMapping> fieldMappings;

    @Override
    public ImportHandler setElementService(ElementService elementService) {
        this.elementService = elementService;
        return this;
    }

    @Override
    public ImportHandler setFieldService(FieldService fieldService) {
        this.fieldService = fieldService;
        return this;
    }

    @Override
    public ImportHandler setMetadataRegistry(MetadataRegistry registry) {
        this.registry = registry;
        return this;
    }



    @Override
    public void importSource(DataSource dataSource) throws IOException {
        try (InputStream input = new FileInputStream(new File(dataSource.getSourceUrl().toURI()))) {
            parseCoconutCSV(input);
        } catch (URISyntaxException ex) {
           throw new RuntimeException(ex.getMessage());
        }
    }

    private void parseCoconutCSV(InputStream input) throws IOException {
        try (var reader = new InputStreamReader(input)) {
            CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT.builder()
                    .setSkipHeaderRecord(true)
                    .get());
            parser.forEach(record -> { parseRecord(record); });
        }
    }

    private void parseRecord(CSVRecord record) {
       for (FieldMapping fieldMapping : fieldMappings) {
           String value = record.get(fieldMapping.getSourceFieldName());
        }
    }
}
