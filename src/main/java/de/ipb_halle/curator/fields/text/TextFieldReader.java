/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.fields.text;

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
public class TextFieldReader {

    @Autowired
    private TextFieldRepository repository;

    /**
     * @param input, for performance reasons, the InputStream should be a BufferedInputStream
     * @throws IOException
     */
    public void read(InputStream input) throws IOException {
        try (var reader = new InputStreamReader(input)) {
            CSVParser parser = CSVParser.parse(reader, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(TextField.HEADER)
                    .setSkipHeaderRecord(true)
                    .get());
            parser.forEach(record -> { parseRecord(record); });
        }
    }

    private void parseRecord(CSVRecord record) {
        TextField field = new TextField(
                UUID.fromString(record.get(TextField.HEADER[0])),
                Integer.parseInt(record.get(TextField.HEADER[1])),
                Integer.parseInt(record.get(TextField.HEADER[2])),
                record.get(TextField.HEADER[3])
        );
        repository.save(field);
    }
}
