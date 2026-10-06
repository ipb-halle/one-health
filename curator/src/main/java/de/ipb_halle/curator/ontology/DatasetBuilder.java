/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 * 
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.ontology;

import java.io.InputStream;
import java.io.UncheckedIOException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author fblocal
 */
public class DatasetBuilder {

    /**
     * Creates a {@link Dataset} from the OWL file with the given name. The
     * file is looked up on the classpath relative to this class (i.e. in the
     * same package).
     *
     * @param owlFile the name of the OWL resource to load
     * @return a {@link Dataset} whose default model contains the statements
     * read from {@code owlFile}
     */
    public Dataset createDataset(String owlFile) {
        try (InputStream input = getClass().getResourceAsStream(owlFile)) {
            if (input == null) {
                throw new IllegalArgumentException("OWL resource not found: " + owlFile);
            }
            Model model = ModelFactory.createDefaultModel();
            RDFDataMgr.read(model, input, Lang.RDFXML);
            return DatasetFactory.wrap(model);
        } catch (java.io.IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
