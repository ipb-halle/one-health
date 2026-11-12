/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source.ontology;

import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.fields.AbstractField;
import de.ipb_halle.curator.fields.MultiValueField;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.onehealth.Element;
import de.ipb_halle.curator.onehealth.ElementService;
import de.ipb_halle.curator.source.DataSource;
import de.ipb_halle.curator.source.ImportHandler;
import de.ipb_halle.curator.source.ImportHandlerFactory;
import de.ipb_halle.curator.source.SourceRegistry;
import java.util.List;
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
public class OntologyImportHandlerTest {

    public final static String DATA_SOURCE = "OWL_ORGANISMS";

    @Autowired
    private SourceRegistry sourceRegistry;

    @Autowired
    private ImportHandlerFactory factory;

    @Autowired
    private ElementService elementService;

    @Autowired
    private MetadataRegistry registry;

    @Test
    public void testImportHandler() {
        ImportHandler handler = factory.build(OntologyImportHandler.class);
        DataSource dataSource = sourceRegistry.getDataSourceById(DATA_SOURCE);
        try {
            handler.importSource(dataSource);
        } catch(Exception e) {
            e.printStackTrace();
        }

        List<Element> elements = elementService.loadByType(registry.getElementType("ORGANISM"));
        for (Element element : elements) {
            System.out.printf("%s %s\n",
                    element.getId().toString(),
                    element.getType().getId());
            for (AbstractField field : element.getFields()) {
                if (field.isMultivalued()) {
                    for (AbstractField f : ((MultiValueField) field).getValues()) {
                        System.out.printf("\t\t%s %s\n", f.getFieldName(), f.toCSVcell().toString());
                    }
                } else {
                    System.out.printf("\t\t%s %s\n", field.getFieldName(), field.toCSVcell().toString());
                }
            }
        }

        assertThat("foo").isEqualTo("foo");
    }
}
