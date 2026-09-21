/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
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
public class ElementWriter {

    @Autowired
    private ElementRepository repository;

    public void write(OutputStream output) throws IOException {
        try (var writer = new OutputStreamWriter(output)) {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(ElementEntity.HEADER)
                    .get());
            repository.findAll().stream().forEach(elementEntity -> {
                writeRecord(printer, elementEntity);
            });
        }
    }

    private void writeRecord(CSVPrinter printer, ElementEntity elementEntity) {
        try {
            printer.printRecord(elementEntity.getId().toString(),
                    String.valueOf(elementEntity.getTypeId()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
