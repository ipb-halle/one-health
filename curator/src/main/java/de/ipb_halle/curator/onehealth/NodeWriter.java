/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    private ElementService service;

    public void writeNodes(ElementType elementType, OutputStream output) throws IOException {
        try (var writer = new OutputStreamWriter(output)) {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.POSTGRESQL_CSV.builder()
                    .setHeader(getHeaders(elementType))
                    .get());
            service.loadByType(elementType)
                    .stream().forEach(dto -> {
                        writeRecord(printer, dto);
                    });
        }
    }

    private String[] getHeaders(ElementType elementType) {
        List<FieldDefinitionDTO> fields = elementType.getFieldDefinitions();
        fields = fields.stream()
                .filter(f -> (f.getGraphExportOrder() != null))
                .collect(Collectors.toList());
        fields.sort((f,g) -> f.getGraphExportOrder().compareTo(g.getGraphExportOrder()));
        List<String> headers = new ArrayList<> ();
        headers.add("id");
        headers.addAll(fields.stream()
                .map(f -> f.getName())
                .collect(Collectors.toList()));
        headers.add("label");
        return headers.toArray(new String[0]);
    }

    private Object[] getCells(ElementDTO dto) {
        List<Object> cells = new ArrayList<> ();
        cells.add(dto.getId());
        cells.addAll(dto.getFields()
                .stream()
                .map(f -> f.toCSVcell())
                .collect(Collectors.toList()));
        cells.add(dto.getType().getLabel());
        return cells.toArray();
    }

    private void writeRecord(CSVPrinter printer, ElementDTO dto) {
        try {
            printer.printRecord(getCells(dto));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
