/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source.ontology;

import de.ipb_halle.curator.fields.AbstractField;
import de.ipb_halle.curator.metadata.FieldDefinition;
import de.ipb_halle.curator.onehealth.Element;
import de.ipb_halle.curator.source.AbstractImportHandler;
import de.ipb_halle.curator.source.DataSource;
import de.ipb_halle.curator.source.ElementMapping;
import java.util.Set;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;

/**
 *
 * @author fbroda
 */
public class OntologyImportHandler extends AbstractImportHandler {

    private final static String PARAMETER_QUERY = "QUERY";

    @Override
    public void importSource(DataSource source) {
        DatasetBuilder builder = new DatasetBuilder();
        Dataset dataset = builder.createDataset(source.getSourceUrl().getFile());
        executeQuery(source, dataset);
    }

    private void executeQuery(DataSource source, Dataset dataset) {
        // xxxxxx
        String query = source.getParameter(PARAMETER_QUERY).toString();
        try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                reset();
                handleElements(source, solution);
                handleFields(source, solution);
            }
        }
    }

    private void handleElements(DataSource dataSource, QuerySolution solution) {
        for (ElementMapping em : (Set<ElementMapping>) dataSource.getElementMappings()) {
            Literal value = solution.getLiteral(em.getSourceFieldName());
            FieldDefinition fieldDefinition = em.getIdentityMappingField();
            AbstractField queryField = createField(null, fieldDefinition, value.getString());
            Element elementDTO = elementService.loadByFieldValue(queryField);
            if (elementDTO == null) {
                createElement(em.getElementType(), fieldDefinition, value.getString());
            } else {
                addElement(elementDTO);
            }
        }

    }

    private void handleFields(DataSource dataSource, QuerySolution solution) {

    }

}
