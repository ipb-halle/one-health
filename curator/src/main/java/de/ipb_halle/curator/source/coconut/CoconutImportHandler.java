/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source.coconut;

import de.ipb_halle.curator.fields.FieldConverter;
import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.fields.FieldService;
import de.ipb_halle.curator.fields.OrderedFieldId;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.onehealth.ElementDTO;
import de.ipb_halle.curator.onehealth.ElementService;
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
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author fblocal
 */
public class CoconutImportHandler implements ImportHandler {

    private ElementService elementService;
    private FieldService fieldService;
    private MetadataRegistry registry;
    private FieldConverter converter;

    private List<FieldMapping> fieldMappings;
    private Map<String, List<ElementDTO>> elementsDTOsByType;

    @Override
    public ImportHandler setElementService(ElementService elementService) {
        this.elementService = elementService;
        return this;
    }

    @Override
    public ImportHandler setFieldConverter(FieldConverter converter) {
        this.converter = converter;
        return this;
    }

    @Override
    public ImportHandler setFieldService(FieldService fieldService) {
        this.fieldService = fieldService;
        return this;
    }

    @Override
    public ImportHandler setMetadataRegistry(MetadataRegistry registry) {
        this.registry = registry;
        return this;
    }

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
        this.elementsDTOsByType = new HashMap<> ();
        handleElements(dataSource, record);
        handleFields(dataSource, record);
    }

    private void handleElements(DataSource dataSource, CSVRecord record) {
        for (ElementMapping em : (Set<ElementMapping>) dataSource.getElementMappings()) {
            String value = record.get(em.getSourceFieldName());
            FieldDefinitionDTO fieldDefinitionDTO = em.getIdentityMappingField();
            FieldDTO queryField = createField(null, fieldDefinitionDTO, value);
            ElementDTO elementDTO = elementService.loadByFieldValue(queryField);
            if (elementDTO == null) {
                createElement(em.getElementType(), fieldDefinitionDTO, value);
            } else {
                addElement(elementDTO);
            }
        }
    }

    private void handleFields(DataSource dataSource, CSVRecord record) {
        for (FieldMapping fm : (Set<FieldMapping>) dataSource.getFieldMappings()) {
            String value = record.get(fm.getSourceFieldName());
            FieldDefinitionDTO fieldDefinitionDTO = fm.getMappingField();
            ElementType elementType = fieldDefinitionDTO.getElementType();
            List<ElementDTO> typedElementDTOs = elementsDTOsByType.get(elementType.getId());
            for (ElementDTO dto : typedElementDTOs) {
                FieldDTO field = createField(dto.getId(), fieldDefinitionDTO, value);
                dto.addField(field);
            }
        }
    }

    private void createElement(ElementType elementType, FieldDefinitionDTO fieldDefinition, String value) {
        List<ElementDTO> typedElementDTOs = elementsDTOsByType.getOrDefault(elementType.getId(), new ArrayList<> ());
        elementsDTOsByType.put(elementType.getId(), typedElementDTOs);
        ElementDTO elementDTO = new ElementDTO(elementType);
        FieldDTO field = createField(elementDTO.getId(), fieldDefinition, value);
        elementDTO.addField(field);
        typedElementDTOs.add(elementDTO);
    }

    private FieldDTO createField(UUID elementId, FieldDefinitionDTO fieldDefinition, String value) {
        return converter.fromString(elementId, fieldDefinition, value);
    }

    private void addElement(ElementDTO elementDTO) {
        ElementType elementType = elementDTO.getType();
        List<ElementDTO> typedElementDTOs = elementsDTOsByType.getOrDefault(elementType.getId(), new ArrayList<> ());
        elementsDTOsByType.put(elementType.getId(), typedElementDTOs);
        typedElementDTOs.add(elementDTO);
    }
}
