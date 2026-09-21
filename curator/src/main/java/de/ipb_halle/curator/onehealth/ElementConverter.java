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

    public ElementEntity createEntity(ElementDTO element) {
        return element.createEntity();
    }

    public ElementDTO createDTO(ElementEntity elementEntity) {
        ElementType type = registry.getElementType(elementEntity.getTypeId());
        return ElementDTO.createDTO(elementEntity, type);
    }

    public List<ElementDTO> createDTOs(List<ElementEntity> elementEntities) {
        return elementEntities.stream().map(e -> createDTO(e)).collect(Collectors.toList());
    }

}
