/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

/**
 *
 * @author fblocal
 */
public class ElementMappingEntity {

    private final Integer id;
    private final String dataSourceId;
    private final String elementTypeId;
    private final String sourceFieldName;
    private final boolean multivalued;        // is the source field multivalued?
    private final String identityMapping;

    public ElementMappingEntity(Integer id, String dataSourceId, String elementTypeId, String sourceFieldName, boolean multivalued, String identityMapping) {
        this.id = id;
        this.dataSourceId = dataSourceId;
        this.elementTypeId = elementTypeId;
        this.sourceFieldName = sourceFieldName;
        this.multivalued = multivalued;
        this.identityMapping = identityMapping;
    }

    public Integer getId() {
        return id;
    }

    public String getElementTypeId() {
        return elementTypeId;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public String getIdentityMapping() {
        return identityMapping;
    }

    public boolean isMultivalued() {
        return multivalued;
    }
}
