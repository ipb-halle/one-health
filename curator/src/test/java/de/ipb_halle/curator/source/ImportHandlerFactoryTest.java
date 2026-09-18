/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

import de.ipb_halle.curator.TestcontainersConfiguration;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 *
 * @author fblocal
 */
@SpringBootTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
public class ImportHandlerFactoryTest {

    @Autowired
    private SourceRegistry sourceRegistry;

    @Autowired
    private ImportHandlerFactory factory;

    @Test
    public void testBuildMethod() {
        DataSource ds = sourceRegistry.getDataSources().get(0);
        ImportHandler handler = factory.build(ds.getHandler());
        assertThat(handler).isNotNull();
        Assertions.assertThatCode(() -> handler.importSource(ds))
                .doesNotThrowAnyException();
    }
}
