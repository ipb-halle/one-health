/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.text;

import de.ipb_halle.curator.DbTestHelper;
import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.onehealth.ElementIoTest;
import de.ipb_halle.curator.onehealth.ElementReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
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
public class TextFieldIoTest {

    public final static String TEXTFIELDS_CSV = "text_fields.csv";
    public final static String TEXTFIELDS_MD5 = "a41d5842e26b0a6d35bfab055fc3676a";

    public final static String TEXTFIELD_NAME = "ORGANISM:primary name";
    public final static String TEXTFIELD_SYNONYM = "ORGANISM:synonym";
    public final static String TEXTFIELD_QUERY = "INSERT INTO text_fields (element_id, field_id, field_order, value) VALUES (?,?,?,?)";

    @Autowired
    private PostgreSQLContainer container;

    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private ElementReader elementReader;

    @Autowired
    private TextFieldWriter writer;

    @Autowired
    private TextFieldReader reader;

    /**
     * Method to generate the initial test data.
     */
    private void createTextFields(DbTestHelper helper) {
        int nameFieldId = registry.getFieldDefinition(TEXTFIELD_NAME).getId();
        int synonymFieldId = registry.getFieldDefinition(TEXTFIELD_SYNONYM).getId();

        helper.dbUpdate(TEXTFIELD_QUERY, UUID.fromString(ElementIoTest.ORGANISM_ID1),
                nameFieldId, 0, "Salvia officinalis");
        helper.dbUpdate(TEXTFIELD_QUERY, UUID.fromString(ElementIoTest.ORGANISM_ID1),
                synonymFieldId, 1, "common sage");
        helper.dbUpdate(TEXTFIELD_QUERY, UUID.fromString(ElementIoTest.ORGANISM_ID1),
                synonymFieldId, 2, "Echter Salbei");
        helper.dbUpdate(TEXTFIELD_QUERY, UUID.fromString(ElementIoTest.ORGANISM_ID1),
                synonymFieldId, 3, "Heilsalbei");
    }

    private void setup(DbTestHelper helper) throws IOException {
        helper.deleteElements();
        InputStream input = ElementReader.class.getResourceAsStream(ElementIoTest.ELEMENTS_CSV);
        elementReader.read(input);
    }

    @Test
    public void testReaderAndWriter() throws Exception {
        try (DbTestHelper helper = new DbTestHelper(container)) {
            setup(helper);

            InputStream input = this.getClass().getResourceAsStream(TEXTFIELDS_CSV);

            /*
             * for generation of test data use this instead of the 'OutputStream output = ByteArrayOutputStream(...);': below
             * createTextFields(helper);
             * OutputStream outputStream = new FileOutputStream("/tmp/text_fields.csv");
             */

            OutputStream outputStream = new ByteArrayOutputStream();
            DigestOutputStream output = new DigestOutputStream(outputStream, MessageDigest.getInstance("MD5"));

            reader.read(input);
            writer.write(output);
            MessageDigest digest = output.getMessageDigest();
            HexFormat format = HexFormat.of().withLowerCase();
            assertThat(format.formatHex(digest.digest())).isEqualTo(TEXTFIELDS_MD5);
        }
    }

}
