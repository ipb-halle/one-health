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
public class DataSourceEntity {

    private final String id;
    private final String description;
    private final String handler;
    private final String sourceUrl;

    public DataSourceEntity(String id, String description, String handler, String sourceUrl) {
        this.id = id;
        this.description = description;
        this.handler = handler;
        this.sourceUrl = sourceUrl;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getHandler() {
        return handler;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }
}
