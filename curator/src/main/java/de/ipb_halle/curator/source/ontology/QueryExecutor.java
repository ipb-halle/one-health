/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source.ontology;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

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
//    public final static String QUERY = """
//            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
//            SELECT ?class ?id
//            WHERE {
//                ?class rdfs:label "Salvia officinalis" .
//                BIND(STRAFTER(STR(?class), "http://purl.obolibrary.org/obo/") AS ?id)
//            }
//            """;
    public final static String QUERY = """
            PREFIX syntax: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX owl: <http://www.w3.org/2002/07/owl#>
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            SELECT ?class WHERE { ?class syntax:type owl:Class }""";
    /**
     * Runs {@link #QUERY} against the given dataset and returns the NCBI
     * taxonomy id of {@code Salvia officinalis} as a string.
     *
     * @param dataset the dataset to query
     * @return the NCBI taxonomy id
     * @throws IllegalStateException if no matching class was found
     */
    public String queryDataset(Dataset dataset) {
        StringBuilder sb = new StringBuilder();
        String id = "";
        try (QueryExecution qexec = QueryExecutionFactory.create(QUERY, dataset)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                // do some experiments here
                Resource resource = solution.getResource("class");
                // id = solution.getLiteral("id").getString();
                sb.append("\nQuery Solution\n");
                printProperties(resource, sb);
            }
            System.out.println(sb.toString());
            return id;
        }
    }

    private void printProperties(Resource res, StringBuilder sb) {
        StmtIterator iter = res.listProperties();
        while(iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Triple triple = stmt.asTriple();
            sb.append("    %s %s %s\n".formatted(
                nodeToString(triple.getSubject()),
                nodeToString(triple.getPredicate()),
                nodeToString(triple.getObject())));
        }
    }

    private String nodeToString(Node node) {
        if (node.isBlank()) {
            return "";
        }
        if (node.isLiteral()) {
            return node.getLiteralLexicalForm();
        }
        if (node.isURI()) {
            return node.getURI();
        }
        return "SOMETHING_ELSE";
    }
}
