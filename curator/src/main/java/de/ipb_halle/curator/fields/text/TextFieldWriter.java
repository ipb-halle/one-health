/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.fields.text;

import de.ipb_halle.curator.fields.IFieldId;
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
public class TextFieldWriter {

    @Autowired
    private TextFieldRepository repository;

    /**
     *
     * @param output, preferably use a BufferedOutputStream
     * @throws IOException
     */
    public void write(OutputStream output) throws IOException {
        try (var writer = new OutputStreamWriter(output)) {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(TextField.HEADER)
                    .get());
            repository.findAll().stream().forEach(textField -> {
                writeRecord(printer, textField);
            });
        }
    }

    private void writeRecord(CSVPrinter printer, TextField textField) {
        try {
            IFieldId fieldId = textField.getId();
            printer.printRecord(fieldId.getElementId().toString(),
                    String.valueOf(fieldId.getFieldId()),
                    String.valueOf(fieldId.getOrder()),
                    textField.getValue());
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
