/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source.coconut;

import de.ipb_halle.curator.source.AbstractImportHandler;
import de.ipb_halle.curator.fields.AbstractField;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinition;
import de.ipb_halle.curator.onehealth.Element;
import de.ipb_halle.curator.source.DataSource;
import de.ipb_halle.curator.source.ElementMapping;
import de.ipb_halle.curator.source.FieldMapping;
import de.ipb_halle.curator.source.ImportHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author fblocal
 */
public class CoconutImportHandler extends AbstractImportHandler implements ImportHandler {

    @Override
    public void importSource(DataSource dataSource) throws IOException {
        try (InputStream input = new FileInputStream(new File(dataSource.getSourceUrl().toURI()))) {
            parseCoconutCSV(dataSource, input);
        } catch (URISyntaxException ex) {
           throw new RuntimeException(ex.getMessage());
        }
    }

    private void parseCoconutCSV(DataSource dataSource, InputStream input) throws IOException {
        try (var reader = new InputStreamReader(input)) {
            CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT.builder()
                    .setSkipHeaderRecord(true)
                    .get());
            parser.forEach(record -> { parseRecord(dataSource, record); });
        }
    }

    private void parseRecord(DataSource dataSource, CSVRecord record) {
        this.elementDTOsByType = new HashMap<> ();
        handleElements(dataSource, record);
        handleFields(dataSource, record);
    }

    private void handleElements(DataSource dataSource, CSVRecord record) {
        for (ElementMapping em : (Set<ElementMapping>) dataSource.getElementMappings()) {
            String value = record.get(em.getSourceFieldName());
            FieldDefinition fieldDefinition = em.getIdentityMappingField();
            AbstractField queryField = createField(null, fieldDefinition, value);
            Element elementDTO = elementService.loadByFieldValue(queryField);
            if (elementDTO == null) {
                createElement(em.getElementType(), fieldDefinition, value);
            } else {
                addElement(elementDTO);
            }
        }
    }

    private void handleFields(DataSource dataSource, CSVRecord record) {
        for (FieldMapping fm : (Set<FieldMapping>) dataSource.getFieldMappings()) {
            String value = record.get(fm.getSourceFieldName());
            FieldDefinition fieldDefinition = fm.getMappingField();
            ElementType elementType = fieldDefinition.getElementType();
            List<Element> typedElementDTOs = elementDTOsByType.get(elementType.getId());
            for (Element dto : typedElementDTOs) {
                AbstractField field = createField(dto.getId(), fieldDefinition, value);
                dto.addField(field);
            }
        }
    }

    private void addElement(Element elementDTO) {
        ElementType elementType = elementDTO.getType();
        List<Element> typedElementDTOs = elementDTOsByType.getOrDefault(elementType.getId(), new ArrayList<> ());
        elementDTOsByType.put(elementType.getId(), typedElementDTOs);
        typedElementDTOs.add(elementDTO);
    }
}
