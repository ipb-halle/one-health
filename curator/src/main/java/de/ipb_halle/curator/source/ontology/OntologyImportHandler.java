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
import de.ipb_halle.curator.source.FieldMapping;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;

/**
 *
 * @author fbroda
 */
public class OntologyImportHandler extends AbstractImportHandler {

    private final static String PARAMETER_QUERY = "QUERY";

    private Element currentElement;

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
                save();
            }
        }
    }

    private void handleElements(DataSource dataSource, QuerySolution solution) {
        for (ElementMapping em : (Collection<ElementMapping>) dataSource.getElementMappings().values()) {
            Literal value = solution.getLiteral(em.getSourceFieldName());
            FieldDefinition fieldDefinition = em.getIdentityMappingField();
            AbstractField queryField = createField(null, fieldDefinition, value.getString());
            currentElement = elementService.loadByFieldValue(queryField);
            if (currentElement == null) {
                currentElement = createElement(em.getElementType(), fieldDefinition, value.getString());
            } else {
                addElement(currentElement);
            }
        }

    }

    private void handleFields(DataSource dataSource, QuerySolution solution) {
        Resource resource = solution.getResource("class");
        StmtIterator iter = resource.listProperties();
        Model model = resource.getModel();
        while(iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Triple triple = stmt.asTriple();
            List<Resource> axioms = findAxioms(model, triple);
            if (! axioms.isEmpty()) {
                addFields(dataSource, triple, axioms);
            } else {
                addField(dataSource, triple);
            }
        }
    }

    private void addField(DataSource dataSource, Triple triple) {
        String fieldValue = nodeToString(triple.getObject());
        FieldMapping fieldMapping = getFieldMapping(dataSource, triple.getPredicate());
        if (fieldMapping != null) {
            FieldDefinition fieldDefinition = fieldMapping.getMappingField();
            AbstractField field = createField(currentElement.getId(), fieldDefinition, fieldValue);
            currentElement.addField(field);
        }
    }

    private void addFields(DataSource dataSource, Triple triple, List<Resource> axioms) {
        for (Resource resource : axioms) {
            addAxiomField(dataSource, triple, resource);
        }
    }

    private void addAxiomField(DataSource dataSource, Triple triple, Resource axiom) {
        StmtIterator iter = axiom.listProperties();
        String fieldValue = nodeToString(triple.getObject());
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Property p = stmt.getPredicate();
            if (! (p.equals(RDF.type)
                    || p.equals(OWL.annotatedSource)
                    || p.equals(OWL.annotatedProperty)
                    || p.equals(OWL.annotatedTarget))) {
                FieldMapping fieldMapping = getFieldMapping(dataSource, stmt.getObject().asNode());
                if (fieldMapping != null) {
                    FieldDefinition fieldDefinition = fieldMapping.getMappingField();
                    AbstractField field = createField(currentElement.getId(), fieldDefinition, fieldValue);
                    currentElement.addField(field);
                }
            }
        }
    }

    private FieldMapping getFieldMapping(DataSource dataSource, Node predicate) {
        String predicateString = nodeToString(predicate);
        Map<String, FieldMapping> mappings = dataSource.getFieldMappings();
        return mappings.get(predicateString);
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
     * @param model
     * @param triple
     * @return
     */
    private List<Resource> findAxioms(Model model, Triple triple) {
        RDFNode subjectNode = model.asRDFNode(triple.getSubject());
        StmtIterator iter = model.listStatements(null, OWL.annotatedSource, subjectNode);
        List<Resource> axioms = new ArrayList<> ();
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            Resource candidateAxiom = stmt.getSubject();
            if (candidateAxiom.hasProperty(RDF.type, OWL.Axiom) &&
                    candidateAxiom.hasProperty(OWL.annotatedProperty, model.asRDFNode(triple.getPredicate())) &&
                    candidateAxiom.hasProperty(OWL.annotatedTarget, model.asRDFNode(triple.getObject()))) {
                axioms.add(candidateAxiom);
            }
        }
        return axioms;
    }

}
