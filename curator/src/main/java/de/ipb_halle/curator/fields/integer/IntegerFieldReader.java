/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.integer;

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
        UUID elementId = UUID.fromString(record.get(IntegerField.HEADER[0]));
        Integer fieldDefinitionId = Integer.valueOf(record.get(IntegerField.HEADER[1]));
        int order = Integer.parseInt(record.get(IntegerField.HEADER[2]));
        Integer value = Integer.valueOf(record.get(IntegerField.HEADER[3]));

        FieldDefinitionDTO fieldDefinition = registry.getFieldDefinition(fieldDefinitionId);
        switch(fieldDefinition.getFieldType().getType()) {
            case INTEGER:
                repository.save(new IntegerField(
                                elementId, fieldDefinitionId, order, value));
                break;
            case ENUM:

                DynEnum dynEnum = registry.getDynEnum(fieldDefinitionId, value);
                repository.save(new DynEnumField(
                        elementId, dynEnum, order).getEntity());
                break;
        }
    }
}
