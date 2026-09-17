/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import java.util.Objects;

/**
 *
 * @author fblocal
 */
public class ElementMapping {

    private final Integer id;
    private final DataSource dataSource;
    private final ElementType elementType;
    private final String sourceFieldName;
    private final boolean multivalued;        // is the source field multivalued?
    private final FieldDefinitionDTO identityMappingField;

    public ElementMapping(ElementMappingEntity em, DataSource ds, ElementType elementType, FieldDefinitionDTO identityMappingField) {
        this.id = em.getId();
        this.sourceFieldName = em.getSourceFieldName();
        this.multivalued = em.isMultivalued();
        this.dataSource = ds;
        this.elementType = elementType;
        this.identityMappingField = identityMappingField;
    }

    public Integer getId() {
        return id;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public FieldDefinitionDTO getIdentityMappingField() {
        return identityMappingField;
    }

    public boolean isMultivalued() {
        return multivalued;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final ElementMapping other = (ElementMapping) obj;
        return Objects.equals(this.id, other.id);
    }
}
