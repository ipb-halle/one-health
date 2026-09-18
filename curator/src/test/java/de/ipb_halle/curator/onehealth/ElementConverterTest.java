/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.fields.AbstractField;
import de.ipb_halle.curator.fields.text.TextFieldEntity;
import de.ipb_halle.curator.fields.text.TextField;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinition;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.util.ArrayList;
import java.util.List;
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
public class ElementConverterTest {

    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private ElementConverter converter;

    private AbstractField createTextField(ElementDTO e, String value) {
        FieldDefinition fd = registry.getFieldDefinition("ORGANISM:primary name");
        TextFieldEntity f = new TextFieldEntity(e.getId(), fd.getId(), 0, value);
        return TextField.createDTO(f, fd);
    }

    @Test
    public void testConverter() {
        List<Element> elements = new ArrayList<> ();

        ElementType type = registry.getElementType("ORGANISM");
        ElementDTO dto1 = new ElementDTO(type);
        dto1.addField(createTextField(dto1, "Hello World"));
        ElementDTO dto2 = new ElementDTO(type);
        dto2.addField(createTextField(dto2, "Foo"));

        Element e = converter.createEntity(dto1);
        elements.add(e);

        assertThat(e.getId()).isEqualTo(dto1.getId());
        assertThat(e.getTypeId()).isEqualTo(type.getId());

        elements.add(converter.createEntity(dto2));
        assertThat(converter.createDTOs(elements).size()).isEqualTo(2);

        ElementDTO dto3 = converter.createDTO(elements.get(0));
        assertThat(dto3.getId()).isEqualTo(dto1.getId());
    }
}
