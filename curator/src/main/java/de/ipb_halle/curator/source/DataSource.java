/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author fblocal
 */
public class DataSource <T extends ImportHandler> {

    private final String id;
    private final String description;
    private final Class handler;
    private final URL sourceUrl;
    private final Set<ElementMapping> elementMappings;
    private final Set<FieldMapping> fieldMappings;
    private final Map<String, Object> parameterMappings;

    public DataSource(DataSourceEntity ds) throws MalformedURLException, ClassNotFoundException {
        this.id = ds.getId();
        this.description = ds.getDescription();
        this.handler = Class.forName(ds.getHandler());
        if (! ImportHandler.class.isAssignableFrom(this.handler)) {
            throw new ClassNotFoundException("Handler class does not implement interface ImportHandler");
        }
        this.sourceUrl = new URL(ds.getSourceUrl());
        this.elementMappings = new HashSet<> ();
        this.fieldMappings = new HashSet<> ();
        this.parameterMappings = new HashMap<> ();
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Class<T> getHandler() {
        return handler;
    }

    public Object getParameter(String key) {
        return parameterMappings.get(key);
    }

    public URL getSourceUrl() {
        return sourceUrl;
    }

    public void addElementMapping(ElementMapping em) {
        elementMappings.add(em);
    }

    public Set<ElementMapping> getElementMappings() {
        return elementMappings;
    }

    public void addFieldMapping(FieldMapping fm) {
        fieldMappings.add(fm);
    }

    public Set<FieldMapping> getFieldMappings() {
        return fieldMappings;
    }
}
