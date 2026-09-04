/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.fields.text.TextField;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 *
 * @author fblocal
 */
@SpringBootTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
public class ElementDTOTest {

    private final static int ELEMENT_TYPE_ID = 1;
    private final static int FIELD_DEFINITION_ID = 1;

    @Autowired
    private MetadataRegistry registry;

    @Test
    public void testElementDTO() {
        UUID id1 = UUID.randomUUID();
        ElementType type = registry.getElementType(ELEMENT_TYPE_ID);
        ElementDTO dto = new ElementDTO(id1, type);
        assertThat(dto.getFields().size()).isEqualTo(0);

        FieldDefinitionDTO fieldDef = registry.getFieldDefinition(FIELD_DEFINITION_ID);
        TextField field = new TextField(id1, fieldDef.getId(), 0, "Sample Organism");
        dto.addField(field);
        assertThat(dto.getFields().size()).isEqualTo(1);

        Element element = dto.createElement();
        assertThat(element.getId()).isEqualByComparingTo(id1);
        assertThat(element.getTypeId()).isEqualTo(type.getId());

        ElementType type2 = registry.getElementType(element.getTypeId());
        ElementDTO dto2 = ElementDTO.createElementDTO(element, type2);
        assertThat(dto2.getId()).isEqualByComparingTo(id1);
        assertThat(dto2.getType().getId()).isEqualTo(type.getId());
        assertThat(dto2.getFields().size()).isEqualTo(0);
    }
}
