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
public class FieldMappingEntity {

    private final Integer id;
    private final String dataSourceId;
    private final String sourceFieldName;
    private final String mapping;
    private final boolean multivalued;

    public FieldMappingEntity(Integer id, String dataSourceId, String sourceFieldName, String mapping, boolean multivalued) {
        this.id = id;
        this.dataSourceId = dataSourceId;
        this.sourceFieldName = sourceFieldName;
        this.mapping = mapping;
        this.multivalued = multivalued;
    }

    public Integer getId() {
        return id;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public String getMapping() {
        return mapping;
    }

    public boolean isMultivalued() {
        return multivalued;
    }
}
