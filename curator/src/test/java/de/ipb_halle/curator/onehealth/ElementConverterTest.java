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

    private AbstractField createTextField(Element element, String value) {
        FieldDefinition fd = registry.getFieldDefinition("ORGANISM:primary name");
        TextFieldEntity f = new TextFieldEntity(element.getId(), fd.getId(), 0, value);
        return TextField.createDTO(f, fd);
    }

    @Test
    public void testConverter() {
        List<ElementEntity> elementEntities = new ArrayList<> ();

        ElementType type = registry.getElementType("ORGANISM");
        Element element1 = new Element(type);
        element1.addField(createTextField(element1, "Hello World"));
        Element element2 = new Element(type);
        element2.addField(createTextField(element2, "Foo"));

        ElementEntity elementEntity = converter.createEntity(element1);
        elementEntities.add(elementEntity);

        assertThat(elementEntity.getId()).isEqualTo(element1.getId());
        assertThat(elementEntity.getTypeId()).isEqualTo(type.getId());

        elementEntities.add(converter.createEntity(element2));
        assertThat(converter.createDTOs(elementEntities).size()).isEqualTo(2);

        Element element3 = converter.createDTO(elementEntities.get(0));
        assertThat(element3.getId()).isEqualTo(element1.getId());
    }
}
