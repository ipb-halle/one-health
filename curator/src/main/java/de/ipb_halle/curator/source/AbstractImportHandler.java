/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

import de.ipb_halle.curator.fields.FieldConverter;
import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.fields.FieldService;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinition;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.onehealth.ElementDTO;
import de.ipb_halle.curator.onehealth.ElementService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
public abstract class AbstractImportHandler implements ImportHandler {

    protected ElementService elementService;
    protected FieldService fieldService;
    protected MetadataRegistry registry;
    protected FieldConverter converter;

    protected Map<String, List<ElementDTO>> elementDTOsByType = new HashMap<> ();

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

    protected ElementDTO createElement(ElementType elementType, FieldDefinition fieldDefinition, String value) {
        List<ElementDTO> typedElementDTOs = elementDTOsByType.getOrDefault(elementType.getId(), new ArrayList<> ());
        elementDTOsByType.put(elementType.getId(), typedElementDTOs);
        ElementDTO elementDTO = new ElementDTO(elementType);
        FieldDTO field = createField(elementDTO.getId(), fieldDefinition, value);
        elementDTO.addField(field);
        typedElementDTOs.add(elementDTO);
        return elementDTO;
    }

    protected FieldDTO createField(UUID elementId, FieldDefinition fieldDefinition, String value) {
        return converter.fromString(elementId, fieldDefinition, value);
    }
}
