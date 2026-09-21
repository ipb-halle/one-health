/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author fblocal
 */
@Component
public class ElementReader {

    @Autowired
    private ElementRepository repository;

    public void read(InputStream input) throws IOException {
        try (var reader = new InputStreamReader(input)) {
            CSVParser parser = CSVParser.parse(reader, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(ElementEntity.HEADER)
                    .setSkipHeaderRecord(true)
                    .get());
            parser.forEach(record -> { parseRecord(record); });
        }
    }

    private void parseRecord(CSVRecord record) {
        ElementEntity elementEntity = new ElementEntity(
                UUID.fromString(record.get(ElementEntity.HEADER[0])),
                record.get(ElementEntity.HEADER[1])
        );
        repository.save(elementEntity);
    }
}
