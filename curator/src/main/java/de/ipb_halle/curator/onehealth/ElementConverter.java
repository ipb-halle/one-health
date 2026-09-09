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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author fblocal
 */
@Component
public class ElementConverter {

    @Autowired
    private MetadataRegistry registry;

    public Element createEntity(ElementDTO elementDTO) {
        return elementDTO.createEntity();
    }

    public ElementDTO createDTO(Element element) {
        ElementType type = registry.getElementType(element.getTypeId());
        return ElementDTO.createDTO(element, type);
    }

    public List<ElementDTO> createDTOs(List<Element> elements) {
        return elements.stream().map(e -> createDTO(e)).collect(Collectors.toList());
    }

}
