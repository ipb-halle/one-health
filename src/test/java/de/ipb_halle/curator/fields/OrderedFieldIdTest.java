/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health Project.
 */
package de.ipb_halle.curator.fields;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author fblocal
 */
public class OrderedFieldIdTest {
    @Test
    public void testFieldId_EqualsAndHashcode() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        OrderedFieldId f1 = new OrderedFieldId(id1, 1, 5);
        OrderedFieldId f2 = new OrderedFieldId(id1, 1, 5);
        OrderedFieldId f3 = new OrderedFieldId(id1, 1, 8);
        OrderedFieldId f4 = new OrderedFieldId(id1, 2, 0);
        OrderedFieldId f5 = new OrderedFieldId(id2, 1, 0);

        assertThat(f1.getElementId()).isEqualTo(id1);
        assertThat(f1.getFieldId()).isEqualTo(1);
        assertThat(f1.getOrder()).isEqualTo(5);
        assertThat(f1.hashCode()).isEqualTo(f2.hashCode());
        assertThat(f1.hashCode()).isNotEqualTo(f3.hashCode());
        assertThat(f1.hashCode()).isNotEqualTo(f4.hashCode());
        assertThat(f1.hashCode()).isNotEqualTo(f5.hashCode());
        assertThat(f3.hashCode()).isNotEqualTo(f4.hashCode());
        assertThat(f3.hashCode()).isNotEqualTo(f5.hashCode());

        assertThat(f1.equals(null)).isFalse();
        assertThat(f1.equals(id1)).isFalse();
        assertThat(f1.equals(f3)).isFalse();
        assertThat(f1.equals(f4)).isFalse();
        assertThat(f1.equals(f5)).isFalse();
        assertThat(f1.equals(f1)).isTrue();
        assertThat(f1.equals(f2)).isTrue();
        f2.setOrder(6);
        assertThat(f1.equals(f2)).isFalse();
    }

}
