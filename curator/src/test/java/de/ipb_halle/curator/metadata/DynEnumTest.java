/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author fblocal
 */
public class DynEnumTest {

    private final static int FIELD_ID = 8;
    private final static String LABEL = "HELLO";
    private final static String DESCRIPTION = "Hello World!";

    @Test
    public void testDynEnum() {
        final DynEnum dynEnum = new DynEnum(null, FIELD_ID, LABEL, DESCRIPTION);
        assertThat(dynEnum.getFieldDefinitionId()).isEqualTo(FIELD_ID);
        assertThat(dynEnum.getLabel()).isEqualTo(LABEL);
        assertThat(dynEnum.getDescription()).isEqualTo(DESCRIPTION);

        Assertions.assertThatCode(() -> dynEnum.setId(998))
                .doesNotThrowAnyException();
        Assertions.assertThatThrownBy(() -> dynEnum.setId(999))
                .isInstanceOf(IllegalStateException.class);
    }
}
