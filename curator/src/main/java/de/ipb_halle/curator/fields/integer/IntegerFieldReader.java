/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.integer;

import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.metadata.DynEnum;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import de.ipb_halle.curator.metadata.MetadataRegistry;
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
public class IntegerFieldReader {

    @Autowired
    private IntegerFieldRepository repository;

    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private IntegerFieldConverter converter;

    /**
     * @param input, for performance reasons, the InputStream should be a BufferedInputStream
     * @throws IOException
     */
    public void read(InputStream input) throws IOException {
        try (var reader = new InputStreamReader(input)) {
            CSVParser parser = CSVParser.parse(reader, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(IntegerField.HEADER)
                    .setSkipHeaderRecord(true)
                    .get());
            parser.forEach(record -> { parseRecord(record); });
        }
    }

    private void parseRecord(CSVRecord record) {
        IntegerField field = new IntegerField(
                UUID.fromString(record.get(IntegerField.HEADER[0])),
                Integer.parseInt(record.get(IntegerField.HEADER[1])),
                Integer.parseInt(record.get(IntegerField.HEADER[2])),
                Integer.valueOf(record.get(IntegerField.HEADER[3])));
        // validation by turning into DTO
        FieldDTO dto = converter.createDTO(field);
        if (dto != null) {
            repository.save(field);
        }
    }
}
