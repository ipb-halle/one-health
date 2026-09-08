/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

/**
 *
 * @author fblocal
 */
public class DynEnum {
    private final Integer id;
    private final Integer fieldDefinitionId;
    private final String label;
    private final String description;

    public DynEnum(Integer id, Integer fieldDefinitionId, String label, String description) {
        this.id = id;
        this.fieldDefinitionId = fieldDefinitionId;
        this.label = label;
        this.description = description;
    }


    public Integer getId() {
        return id;
    }

    public Integer getFieldDefinitionId() {
        return fieldDefinitionId;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
