/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source.testHandler;

import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import de.ipb_halle.curator.onehealth.Element;
import de.ipb_halle.curator.onehealth.ElementDTO;
import de.ipb_halle.curator.source.AbstractImportHandler;
import de.ipb_halle.curator.source.DataSource;
import de.ipb_halle.curator.source.ElementMapping;
import de.ipb_halle.curator.source.FieldMapping;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;

/**
 * Simple ImportHandler for testing purposes
 * @author fblocal
 */
public class TestOrganismImportHandler extends AbstractImportHandler {

    @Override
    public void importSource(DataSource dataSource) throws IOException {
        InputStream input = this.getClass().getResourceAsStream(dataSource.getSourceUrl().getFile());
        parseSource(dataSource, input);
    }
    
    private void parseSource(DataSource dataSource, InputStream input) throws IOException {
        try (var reader = new InputStreamReader(input)) {
            CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get());
            parser.forEach(record ->  parseRecord(dataSource, record));
        }
    }

    private void parseRecord(DataSource dataSource, CSVRecord record) {
        ElementDTO elementDTO = parseElement(dataSource, record);
        parseFields(dataSource, record, elementDTO);
        elementService.save(elementDTO);
    }

    private ElementDTO parseElement(DataSource dataSource, CSVRecord record) {
        for (ElementMapping em : (Set<ElementMapping>) dataSource.getElementMappings()) {
            String value = record.get(em.getSourceFieldName());
            FieldDefinitionDTO fieldDefinitionDTO = em.getIdentityMappingField();
            FieldDTO queryField = createField(null, fieldDefinitionDTO, value);
            ElementDTO elementDTO = elementService.loadByFieldValue(queryField);
            if (elementDTO == null) {
                return createElement(em.getElementType(), fieldDefinitionDTO, value);
            }
            return elementDTO;
        }
        return null;
    }

    private void parseFields(DataSource dataSource, CSVRecord record, ElementDTO elementDTO) {
        for (FieldMapping fm : (Set<FieldMapping>) dataSource.getFieldMappings()) {
            String value = record.get(fm.getSourceFieldName());
            FieldDefinitionDTO fieldDefinitionDTO = fm.getMappingField();
            if (elementDTO.getField(fieldDefinitionDTO.getName()) == null) {
                FieldDTO field = createField(elementDTO.getId(), fieldDefinitionDTO, value);
                elementDTO.addField(field);
            }
        }
    }
}
