/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source.ontology;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

/**
 * Executes SPARQL queries against a Jena {@link Dataset}.
 *
 * @author fblocal
 */
public class QueryExecutor {

    /**
     * SPARQL query which looks up the class with the {@code rdfs:label}
     * {@code "Salvia officinalis"} and extracts the NCBI taxonomy id from its
     * IRI (the part following {@code obo/}, e.g. {@code NCBITaxon_38868}).
     */
    public final static String QUERY = """
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            SELECT ?class ?id
            WHERE {
                ?class rdfs:label "Salvia officinalis" .
                BIND(STRAFTER(STR(?class), "http://purl.obolibrary.org/obo/") AS ?id)
            }
            """;

    /**
     * Runs {@link #QUERY} against the given dataset and returns the NCBI
     * taxonomy id of {@code Salvia officinalis} as a string.
     *
     * @param dataset the dataset to query
     * @return the NCBI taxonomy id
     * @throws IllegalStateException if no matching class was found
     */
    public String queryDataset(Dataset dataset) {
        try (QueryExecution qexec = QueryExecutionFactory.create(QUERY, dataset)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                return solution.getLiteral("id").getString();
            }
        }
        throw new IllegalStateException("No NCBI taxonomy id found for Salvia officinalis");
    }
}
