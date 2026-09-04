/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.onehealth;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author fblocal
 */
@Component
public class RelationWriter {

    @Autowired
    private RelationRepository repository;

    public void write(OutputStream output) throws IOException {
        try (var writer = new OutputStreamWriter(output)) {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(Relation.HEADER)
                    .get());
            repository.findAll().stream().forEach(relation -> {
                writeRecord(printer, relation);
            });
        }
    }

    private void writeRecord(CSVPrinter printer, Relation relation) {
        try {
            printer.printRecord(relation.getId().getLeftId().toString(),
                    relation.getId().getRelationId().toString(),
                    relation.getId().getRightId().toString());
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
