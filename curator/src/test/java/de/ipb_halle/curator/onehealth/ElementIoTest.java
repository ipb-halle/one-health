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
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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
 * Round trip test of element reader and writer
 * @author fblocal
 */
@SpringBootTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
public class ElementIoTest {

    public final static String ELEMENTS_CSV = "elements.csv";
    public final static String ELEMENTS_MD5 = "0d858d1ebf38602e3768d837e041fda6";

    public final static String ORGANISM_ID1 = "20452447-b178-4fc2-a3bc-9ee03fe9bb19";
    public final static String COMPOUND_ID1 = "8d459233-5099-4c65-b928-a1df86484d2a";
    public final static String DISEASE_ID1 = "82d0ea8f-7249-4ef3-8d26-0a907301abe8";
    public final static String RELATION_ID1 = "c3a66023-45d9-43a3-9b11-670960cbbd1a";

    @Autowired
    private PostgreSQLContainer container;

    @Autowired
    private ElementWriter writer;

    @Autowired
    private ElementReader reader;

    /**
     * Method to generate the initial test data.
     */
    private void createElements(DbTestHelper helper) {
        helper.createElement(1);
        helper.createElement(2);
        helper.createElement(3);
        helper.createElement(4);
    }

    @Test
    public void testReaderAndWriter() throws Exception {
        try (DbTestHelper helper = new DbTestHelper(container)) {
            helper.deleteElements();
            InputStream input = this.getClass().getResourceAsStream(ELEMENTS_CSV);

            /*
             * for generation of test data use this instead of the 'OutputStream output = ...' below
             *
             *      createElements(helper);
             *      OutputStream outputStream = new FileOutputStream("/tmp/elements.csv");
             */

            OutputStream outputStream = new ByteArrayOutputStream();
            DigestOutputStream output = new DigestOutputStream(outputStream, MessageDigest.getInstance("MD5"));

            reader.read(input);
            writer.write(output);
            MessageDigest digest = output.getMessageDigest();
            HexFormat format = HexFormat.of().withLowerCase();
            assertThat(format.formatHex(digest.digest())).isEqualTo(ELEMENTS_MD5);
        }
    }
}
