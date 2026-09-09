/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.DbTestHelper;
import de.ipb_halle.curator.TestcontainersConfiguration;
import static de.ipb_halle.curator.fields.integer.IntegerFieldIoTest.INTEGERFIELD_NAME;
import static de.ipb_halle.curator.fields.integer.IntegerFieldIoTest.INTEGERFIELD_QUERY;
import de.ipb_halle.curator.fields.text.TextFieldIoTest;
import de.ipb_halle.curator.fields.text.TextFieldReader;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.io.IOException;
import java.io.InputStream;
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
    private ElementReader elementReader;

    @Autowired
    private TextFieldReader textFieldReader;

    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private ElementService service;

    /**
     * Method to generate the initial test data.
     */
    private void setup(DbTestHelper helper) throws IOException {
        helper.deleteElements();
        InputStream input = ElementReader.class.getResourceAsStream(ElementIoTest.ELEMENTS_CSV);
        elementReader.read(input);
        input = TextFieldReader.class.getResourceAsStream(TextFieldIoTest.TEXTFIELDS_CSV);
        textFieldReader.read(input);
    }

    @Test
    public void testElementService() throws Exception {
        ElementType type = registry.getElementType(1);
        try (DbTestHelper helper = new DbTestHelper(container)) {
            setup(helper);
            List<ElementDTO> dtos = service.loadByType(type);
            assertThat(dtos.size()).isEqualTo(1);
            assertThat(dtos.get(0).getFields().size()).isEqualTo(4);
        }
    }

}
