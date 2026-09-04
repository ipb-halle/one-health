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
public class RelationTest {
    @Autowired
    private PostgreSQLContainer container;

    @Test
    public void testRelation() {
        try (DbTestHelper helper = new DbTestHelper(container)) {

            helper.deleteElements();
            UUID id1 = helper.createElement(1);
            UUID id2 = helper.createElement(3);
            UUID id3 = helper.createElement(4);
            UUID id4 = helper.createElement(1);

            Relation rel1 = new Relation(id1, id2, id3);
            Relation rel2 = new Relation(id4, id2, id3);
            RelationId rId1 = rel1.getId();
            RelationId rId2 = rel2.getId();

            assertThat(rId1).isNotEqualTo(rId2);
            assertThat(rId1.getLeftId()).isEqualTo(id1);
            assertThat(rId1.getRelationId()).isEqualTo(id2);
            assertThat(rId1.getRightId()).isEqualTo(id3);
        }
    }
}
