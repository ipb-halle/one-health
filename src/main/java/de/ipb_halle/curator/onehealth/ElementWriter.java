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
public class ElementWriter {

    @Autowired
    private ElementRepository repository;

    public void write(OutputStream output) throws IOException {
        try (var writer = new OutputStreamWriter(output)) {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(Element.HEADER)
                    .get());
            repository.findAll().stream().forEach(element -> {
                writeRecord(printer, element);
            });
        }
    }

    private void writeRecord(CSVPrinter printer, Element element) {
        try {
            printer.printRecord(element.getId().toString(),
                    String.valueOf(element.getTypeId()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
