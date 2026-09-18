/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.DbTestHelper;
import de.ipb_halle.curator.TestDataCreator;
import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.fields.IFieldId;
import de.ipb_halle.curator.fields.OrderedFieldId;
import de.ipb_halle.curator.fields.text.TextFieldEntity;
import de.ipb_halle.curator.fields.text.TextField;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinition;
import de.ipb_halle.curator.metadata.FieldType;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 *
 * @author fblocal
 */
@SpringBootTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
public class ElementServiceTest {

    @Autowired
    private PostgreSQLContainer container;

    @Autowired
    private TestDataCreator creator;

    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private ElementService service;


    @Test
    public void testLoadByType() throws Exception {
        ElementType type = registry.getElementType("ORGANISM");
        try (DbTestHelper helper = new DbTestHelper(container)) {
            creator.setupFull(helper);
            List<ElementDTO> dtos = service.loadByType(type);
            assertThat(dtos.size()).isEqualTo(1);
            assertThat(dtos.get(0).getFields().size()).isEqualTo(4);
        }
    }

    @Test
    public void testLoadByField() throws Exception {
        try (DbTestHelper helper = new DbTestHelper(container)) {
            creator.setupFull(helper);
            FieldDefinition fieldDef = registry.getFieldDefinition("ORGANISM:synonym");
            IFieldId fieldId = new OrderedFieldId(null, fieldDef.getId(), 0);
            TextFieldEntity textField = new TextFieldEntity(fieldId, "common sage");
            TextField field = TextField.createDTO(textField, fieldDef);

            ElementDTO elementDTO = service.loadByFieldValue(field);
            assertThat(elementDTO).isNotNull();
            assertThat(elementDTO.getId()).isEqualTo(UUID.fromString(ElementIoTest.ORGANISM_ID1));
        }
    }
}
