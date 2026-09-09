/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author fblocal
 */
@Component
public class NodeWriter {
    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private ElementRepository repository;

    public void writeNodes(ElementType elementType, OutputStream output) throws IOException {
        String[] headers = getHeaders(elementType);

        try (var writer = new OutputStreamWriter(output)) {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(headers)
                    .get());
            repository.findElementsByType(elementType.getId())
                    .stream().forEach(element -> {
                        ElementDTO dto = ElementDTO.createElementDTO(element, elementType);
                        getFields(dto);
                        writeRecord(printer, dto);
                    });
        }

        repository.findElementsByType(elementType.getId());
    }

    private String[] getHeaders(ElementType elementType) {
        return  new String[] {"id", "label"};
    }

    private void getFields(ElementDTO dto) {

    }

    private void writeRecord(CSVPrinter printer, ElementDTO dto) {
        try {
            printer.printRecord(dto.getId().toString(),
                    String.valueOf(dto.getType().getLabel()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
