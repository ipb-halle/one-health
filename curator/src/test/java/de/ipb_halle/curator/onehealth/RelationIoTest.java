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
 * Round trip test of element reader and writer
 * @author fblocal
 */
@SpringBootTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
public class RelationIoTest {

    public final static String RELATIONS_CSV = "relations.csv";
    public final static String RELATIONS_MD5 = "c4b3c75da97014d63fba861fa340c5d5";
    public final static String RELATIONS_QUERY = "INSERT INTO relations (left_id, relation_id, right_id) VALUES (?,?,?)";
    

    @Autowired
    private PostgreSQLContainer container;

    @Autowired
    private RelationWriter writer;

    @Autowired
    private RelationReader reader;

    @Autowired
    private ElementReader elementReader;

    /**
     * Method to generate the initial test data.
     */
    private void createRelations(DbTestHelper helper) {
        helper.dbUpdate(RELATIONS_QUERY,
                UUID.fromString(ElementIoTest.ORGANISM_ID1),
                UUID.fromString(ElementIoTest.RELATION_ID1),
                UUID.fromString(ElementIoTest.DISEASE_ID1));
        helper.dbUpdate(RELATIONS_QUERY,
                UUID.fromString(ElementIoTest.COMPOUND_ID1),
                UUID.fromString(ElementIoTest.RELATION_ID1),
                UUID.fromString(ElementIoTest.DISEASE_ID1));
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
            InputStream input = this.getClass().getResourceAsStream(RELATIONS_CSV);

            /*
             * for generation of test data use this instead of the 'OutputStream output = ...' below
             *
             *     createRelations(helper);
             *     OutputStream outputStream = new FileOutputStream("/tmp/relations.csv");
             */

            OutputStream outputStream = new ByteArrayOutputStream();
            DigestOutputStream output = new DigestOutputStream(outputStream, MessageDigest.getInstance("MD5"));

            reader.read(input);
            writer.write(output);
            MessageDigest digest = output.getMessageDigest();
            HexFormat format = HexFormat.of().withLowerCase();
            assertThat(format.formatHex(digest.digest())).isEqualTo(RELATIONS_MD5);
        }
    }
}
