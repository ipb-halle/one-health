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
public class DataSourceParameterEntity {

    private final String id;
    private final String description;
    private final String name;
    private final String value;

    public DataSourceParameterEntity(String id, String name, String description, String value) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
