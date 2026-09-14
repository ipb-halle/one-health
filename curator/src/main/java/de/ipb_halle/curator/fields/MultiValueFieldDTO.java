/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 * 
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author fblocal
 */
public class MultiValueFieldDTO extends FieldDTO {

    private List<FieldDTO> values;
    
    public MultiValueFieldDTO(FieldDTO first) {
        super(first.getId(), first.getFieldDefinition());
        values = new LinkedList<> ();
        values.add(first);
    }
    
    @Override
    public Object toCSVcell() {
        throw new UnsupportedOperationException("Multivalued Fields cannot be transformed into Neo4J nodes.");
    }
    
    public void addValue(FieldDTO value) {
        values.add(value);
    } 
    
    public List<FieldDTO> getValues() {
        return values;
    }
}
