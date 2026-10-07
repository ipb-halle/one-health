/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source.ontology;

import org.apache.jena.query.Dataset;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author fblocal
 */
public class DatasetBuilderTest {

    public final static String OWL_FILE = "test.owl";

    @Test
    public void testCreateDataset() {
        DatasetBuilder builder = new DatasetBuilder();

        Dataset dataset = builder.createDataset(OWL_FILE);

        assertThat(dataset).isNotNull();
        assertThat(dataset.getDefaultModel()).isNotNull();
        assertThat(dataset.getDefaultModel().isEmpty()).isFalse();

        QueryExecutor executor = new QueryExecutor();
        String id = executor.queryDataset(dataset);
        assertThat(id).isEqualTo("NCBITaxon_38868");
    }
}
