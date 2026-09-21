/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.fields.text.TextFieldEntity;
import de.ipb_halle.curator.fields.text.TextField;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinition;
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
public class ElementTest {

    private final static String ELEMENT_TYPE_ID = "ORGANISM";
    private final static int FIELD_DEFINITION_ID = 1;

    @Autowired
    private MetadataRegistry registry;

    @Test
    public void testElementDTO() {
        UUID id1 = UUID.randomUUID();
        ElementType type = registry.getElementType(ELEMENT_TYPE_ID);
        Element element = new Element(id1, type);
        assertThat(element.getFields().size()).isEqualTo(0);

        FieldDefinition fieldDef = registry.getFieldDefinition(FIELD_DEFINITION_ID);
        TextFieldEntity field = new TextFieldEntity(id1, fieldDef.getId(), 0, "Sample Organism");
        element.addField(TextField.createDTO(field, fieldDef));
        assertThat(element.getFields().size()).isEqualTo(1);

        ElementEntity elementEntity = element.createEntity();
        assertThat(elementEntity.getId()).isEqualByComparingTo(id1);
        assertThat(elementEntity.getTypeId()).isEqualTo(type.getId());

        ElementType type2 = registry.getElementType(elementEntity.getTypeId());
        Element element2 = Element.createDTO(elementEntity, type2);
        assertThat(element2.getId()).isEqualByComparingTo(id1);
        assertThat(element2.getType().getId()).isEqualTo(type.getId());
        assertThat(element2.getFields().size()).isEqualTo(0);

        element = new Element(type);
        assertThat(element.getId()).isInstanceOf(UUID.class);
    }

    @Test
    public void testElement() {
        ElementEntity elementEntity = new ElementEntity(ELEMENT_TYPE_ID);
        assertThat(elementEntity.getTypeId()).isEqualTo(ELEMENT_TYPE_ID);
        assertThat(elementEntity.getId()).isInstanceOf(UUID.class);
    }
}
