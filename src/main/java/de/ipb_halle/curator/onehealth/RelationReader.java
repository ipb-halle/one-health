/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.onehealth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class RelationReader {

    @Autowired
    private RelationRepository repository;

    public void read(Path csvPath) throws IOException {
        try (var reader = Files.newBufferedReader(csvPath)) {
            CSVParser parser = CSVParser.parse(reader, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(Relation.HEADER)
                    .get());
            parser.forEach(record -> { parseRecord(record); });
        }
    }

    private void parseRecord(CSVRecord record) {
        Relation relation = new Relation(
                UUID.fromString(record.get(Relation.HEADER[0])),
                UUID.fromString(record.get(Relation.HEADER[1])),
                UUID.fromString(record.get(Relation.HEADER[2]))
        );
        repository.save(relation);
    }
}
