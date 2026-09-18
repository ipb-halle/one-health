/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

import de.ipb_halle.curator.metadata.FieldDefinition;
import java.util.Objects;

/**
 *
 * @author fblocal
 */
public class FieldMapping {

    private final Integer id;
    private final DataSource dataSource;
    private final String sourceFieldName;
    private final FieldDefinition mappingField;
    private final boolean multivalued;

    public FieldMapping(FieldMappingEntity fm, DataSource dataSource, FieldDefinition mappingField) {
        this.id = fm.getId();
        this.sourceFieldName = fm.getSourceFieldName();
        this.multivalued = fm.isMultivalued();
        this.dataSource = dataSource;
        this.mappingField = mappingField;
    }

    public Integer getId() {
        return id;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public FieldDefinition getMappingField() {
        return mappingField;
    }

    public boolean isMultivalued() {
        return multivalued;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FieldMapping other = (FieldMapping) obj;
        return Objects.equals(this.id, other.id);
    }
}
