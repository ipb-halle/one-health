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
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;

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
            return "it worked";
        }
    }

    private void printProperties(Resource res, StringBuilder sb) {
        StmtIterator iter = res.listProperties();
        Model model = res.getModel();
        while(iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Triple triple = stmt.asTriple();
            sb.append("    %s %s %s".formatted(
                nodeToString(triple.getSubject()),
                nodeToString(triple.getPredicate()),
                nodeToString(triple.getObject())));
            Resource axiom = findAxiom(model, triple);
            if (axiom != null) {
                printAnnotation(axiom, sb);
            }
            sb.append("\n");
        }
    }

    private void printAnnotation(Resource axiom, StringBuilder sb) {
        StmtIterator iter = axiom.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Property p = stmt.getPredicate();
            if (! (p.equals(RDF.type)
                    || p.equals(OWL.annotatedSource)
                    || p.equals(OWL.annotatedProperty)
                    || p.equals(OWL.annotatedTarget))) {
                sb.append(" (%s %s)".formatted(
                    nodeToString(p.asNode()),
                    nodeToString(stmt.getObject().asNode())));
            }
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

    /**
     * NOTE: This method is restricted to find the first axiom annotating a given
     * Triple. Subsequent further annotations are ignored.
     * @param model
     * @param triple
     * @return
     */
    private Resource findAxiom(Model model, Triple triple) {
        RDFNode subjectNode = model.asRDFNode(triple.getSubject());
        StmtIterator iter = model.listStatements(null, OWL.annotatedSource, subjectNode);
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            Resource candidateAxiom = stmt.getSubject();
            if (candidateAxiom.hasProperty(RDF.type, OWL.Axiom) &&
                    candidateAxiom.hasProperty(OWL.annotatedProperty, model.asRDFNode(triple.getPredicate())) &&
                    candidateAxiom.hasProperty(OWL.annotatedTarget, model.asRDFNode(triple.getObject()))) {
                return candidateAxiom;
            }
        }
        return null;
    }
}
