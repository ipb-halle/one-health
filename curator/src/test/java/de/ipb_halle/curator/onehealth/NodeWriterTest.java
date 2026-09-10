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
import de.ipb_halle.curator.fields.integer.IntegerFieldIoTest;
import de.ipb_halle.curator.fields.integer.IntegerFieldReader;
import de.ipb_halle.curator.fields.text.TextFieldIoTest;
import de.ipb_halle.curator.fields.text.TextFieldReader;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
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
public class NodeWriterTest {

    public final static String NODEWRITER_MD5 = "e0ab4f8ced3acce4adaa145d454d789d";

    @Autowired
    private NodeWriter nodeWriter;

    @Autowired
    private PostgreSQLContainer container;

    @Autowired
    private MetadataRegistry registry;

    @Autowired
    private ElementReader elementReader;

    @Autowired
    private IntegerFieldReader integerReader;

    @Autowired
    private TextFieldReader textReader;


    private void setup(DbTestHelper helper) throws IOException {
        helper.deleteElements();
        InputStream input = ElementReader.class.getResourceAsStream(ElementIoTest.ELEMENTS_CSV);
        elementReader.read(input);
        input = TextFieldReader.class.getResourceAsStream(TextFieldIoTest.TEXTFIELDS_CSV);
        textReader.read(input);
        input = IntegerFieldReader.class.getResourceAsStream(IntegerFieldIoTest.INTEGERFIELDS_CSV);
        integerReader.read(input);
    }

    @Test
    public void testWriteNodes() throws Exception {
        try (DbTestHelper helper = new DbTestHelper(container)) {
            setup(helper);
            ElementType type = registry.getElementType(1);
            // OutputStream output = new FileOutputStream("/tmp/nodeWriter.csv");
            OutputStream output = OutputStream.nullOutputStream();
            DigestOutputStream digestStream = new DigestOutputStream(output, MessageDigest.getInstance("MD5"));
            nodeWriter.writeNodes(type, digestStream);

            MessageDigest digest = digestStream.getMessageDigest();
            HexFormat format = HexFormat.of().withLowerCase();
            assertThat(format.formatHex(digest.digest())).isEqualTo(NODEWRITER_MD5);
        }
    }
}