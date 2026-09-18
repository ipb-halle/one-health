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
public class SourceRegistryTest {

    @Autowired
    private SourceRegistry sourceRegistry;

    @Test
    public void testDataSource_FailOnDuplicateInitialization() {
        assertThat(sourceRegistry.isInitialized()).isTrue();
        Assertions.assertThatThrownBy(() -> sourceRegistry.initializeDataSources(null))
                .isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> sourceRegistry.initializeElementMappings(null))
                .isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> sourceRegistry.initializeFieldMappings(null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void testDataSource() {
        assertThat(sourceRegistry.getDataSources().size()).isGreaterThan(0);

        DataSource ds = sourceRegistry.getDataSources().get(0);
        assertThat(ds.getElementMappings().size()).isGreaterThan(0);
        assertThat(ds.getFieldMappings().size()).isGreaterThan(2);
        assertThat(ds.getHandler()).isAssignableTo(ImportHandler.class);
        assertThat(ds.getSourceUrl()).isNotNull();
    }
}
