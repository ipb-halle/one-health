/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.onehealth;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author fblocal
 */
public class RelationIdTest {

    public void notEqual(RelationId r1, UUID id1, UUID id2, UUID id3) {
        RelationId r2 = new RelationId(id1, id2, id3);
        assertThat(r1.equals(r2)).isFalse();
        assertThat(r1.hashCode()).isNotEqualTo(r2.hashCode());
    }

    @Test
    public  void testRelationId() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        UUID id4 = UUID.randomUUID();

        RelationId r1 = new RelationId(id1, id2, id3);
        assertThat(r1.equals(r1)).isTrue();
        assertThat(r1.equals(null)).isFalse();
        assertThat(r1.equals(id1)).isFalse();
        assertThat(r1).isEqualTo(r1);
        notEqual(r1, id1, id2, id4);
        notEqual(r1, id1, id4, id3);
        notEqual(r1, id4, id2, id3);
    }

}
