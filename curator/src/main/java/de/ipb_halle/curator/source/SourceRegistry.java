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
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.net.MalformedURLException;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Singleton holder for all immutable metadata maps loaded from the database at startup.
 * Provides read-only access to element_types, field type groupings, and field definitions by name.
 * Order of initialization matters.
 */
@Component
public class SourceRegistry {

    @Autowired
    private MetadataRegistry metadataRegistry;

    private List<DataSource> dataSources;
    private Map<String, DataSource> dataSourcesById;

    private boolean dataSourcesInitialized = false;
    private boolean elementMappingsInitialized = false;
    private boolean fieldMappingsInitialized = false;

    public void initializeDataSources(List<DataSourceEntity> dataSources) {
        if (dataSourcesInitialized) {
            throw new RuntimeException("Duplicate initialization of ElementTypes");
        }
        this.dataSources = dataSources.stream()
                .map(ds -> {
                    try {
                        return new DataSource(ds);
                    } catch(ClassNotFoundException | MalformedURLException ex) {
                        throw new RuntimeException("Misconfigured DataSource", ex);
                    }})
                .collect(Collectors.toList());

        this.dataSourcesById = this.dataSources.stream()
                .collect(Collectors.toMap(DataSource::getId, Function.identity()));
        dataSourcesInitialized = true;
    }

    public void initializeElementMappings(List<ElementMappingEntity> elementMappings) {
        if (! dataSourcesInitialized) {
            throw new RuntimeException("Missing initialization of DataSources");
        }
        if (elementMappingsInitialized) {
            throw new RuntimeException("Duplicate initialization of ElementMappings");
        }
        for( var em : elementMappings) {
            DataSource ds = dataSourcesById.get(em.getDataSourceId());
            ElementType et = metadataRegistry.getElementType(em.getElementTypeId());
            FieldDefinitionDTO identityField = metadataRegistry.getFieldDefinition(em.getIdentityMapping());
            if ((ds == null) || (et == null) || (identityField == null)) {
                throw new IllegalArgumentException("Unresolvable element mapping.");
            }
            ElementMapping elementMapping = new ElementMapping(em, ds, et, identityField);
            ds.addElementMapping(elementMapping);
        }
        elementMappingsInitialized = true;
    }

    public void initializeFieldMappings(List<FieldMappingEntity> fieldMappings) {
        if (! dataSourcesInitialized) {
            throw new RuntimeException("Missing initialization of DataSources");
        }
        if (fieldMappingsInitialized) {
            throw new RuntimeException("Duplicate initialization of FieldMappings");
        }
        for (var fm : fieldMappings) {
            DataSource ds = dataSourcesById.get(fm.getDataSourceId());
            FieldDefinitionDTO field = metadataRegistry.getFieldDefinition(fm.getMapping());
            FieldMapping fieldMapping = new FieldMapping(fm, ds, field);
            if ((ds==null) || (field == null)) {
                throw new IllegalArgumentException("Unresolvable field mapping.");
            }
            ds.addFieldMapping(fieldMapping);
        }
        fieldMappingsInitialized = true;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public boolean isInitialized() {
        return this.dataSourcesInitialized
                && this.elementMappingsInitialized
                && this.fieldMappingsInitialized;
    }
}
