/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

import de.ipb_halle.curator.source.coconut.CoconutImportHandler;
import java.net.MalformedURLException;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author fblocal
 */
public class DataSourceTest {

    public final static String ID = "MY_SOURCE";
    public final static String DESCRIPTION = "Arbitrary data source";
    public final static String HANDLER = CoconutImportHandler.class.getName();
    public final static String SOURCE_URL = "file:///tmp/somefile";
    public final static String INVALID_HANDLER = "INVALID_CLASS_NAME";
    public final static String INVALID_SOURCE_URL = "///////////////";

    @Test
    public void testDataSource() throws Exception {
        final DataSourceEntity srcEntity = new DataSourceEntity(ID, DESCRIPTION, HANDLER, SOURCE_URL);
        assertThat(srcEntity.getId()).isEqualTo(ID);
        assertThat(srcEntity.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(srcEntity.getHandler()).isEqualTo(HANDLER);
        assertThat(srcEntity.getSourceUrl()).isEqualTo(SOURCE_URL);

        DataSource src = new DataSource(srcEntity);
        assertThat(src.getId()).isEqualTo(srcEntity.getId());
        assertThat(src.getDescription()).isEqualTo(srcEntity.getDescription());
        assertThat(src.getElementMappings().size()).isEqualTo(0);
        assertThat(src.getFieldMappings().size()).isEqualTo(0);
        assertThat(src.getSourceUrl().getFile()).isEqualTo("/tmp/somefile");
        assertThat(src.getHandler()).isAssignableTo(ImportHandler.class);
    }

    @Test
    public void testDataSourceExceptions() {
        final DataSourceEntity e1 = new DataSourceEntity(ID, DESCRIPTION, INVALID_HANDLER, SOURCE_URL);
        final DataSourceEntity e2 = new DataSourceEntity(ID, DESCRIPTION, HANDLER, INVALID_SOURCE_URL);

        Assertions.assertThatThrownBy(() -> new DataSource(e1))
                .isInstanceOf(ClassNotFoundException.class);
        Assertions.assertThatThrownBy(() -> new DataSource(e2))
                .isInstanceOf(MalformedURLException .class);
    }
}
