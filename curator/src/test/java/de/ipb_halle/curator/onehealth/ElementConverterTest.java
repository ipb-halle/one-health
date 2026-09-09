/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.fields.text.TextField;
import de.ipb_halle.curator.fields.text.TextFieldDTO;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
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

    private FieldDTO createTextFieldDTO(ElementDTO e, String value) {
        FieldDefinitionDTO fd = registry.getFieldDefinition("ORGANISM:primary name");
        TextField f = new TextField(e.getId(), fd.getId(), 0, value);
        return TextFieldDTO.createDTO(f);
    }

    @Test
    public void testConverter() {
        List<Element> elements = new ArrayList<> ();

        ElementType type = registry.getElementType(1);
        ElementDTO dto1 = new ElementDTO(type);
        dto1.addField(createTextFieldDTO(dto1, "Hello World"));
        ElementDTO dto2 = new ElementDTO(type);
        dto2.addField(createTextFieldDTO(dto2, "Foo"));

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
