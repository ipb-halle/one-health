/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator;

import de.ipb_halle.curator.metadata.MetadataRegistry;
import de.ipb_halle.curator.metadata.MetadataRepository;
import de.ipb_halle.curator.source.SourceRegistry;
import de.ipb_halle.curator.source.SourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Phase 0 of the ETL startup pipeline: loads all metadata from the database
 * into the {@link MetadataRegistry}. Runs before any other CLI runner.
 */
@Service
@Order(0)
public class MetadataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MetadataLoader.class);

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private MetadataRegistry metadataRegistry;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private SourceRegistry sourceRegistry;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading metadata and mappings from database...");

        metadataRegistry.initializeElementTypes(metadataRepository.findAllElementTypes());
        metadataRegistry.initializeFieldDefinitions(metadataRepository.findAllFieldDefinitions());
        metadataRegistry.initializeDynEnums(metadataRepository.findAllDynEnums());
        sourceRegistry.initializeDataSources(sourceRepository.findAllDataSources());
        sourceRegistry.initializeElementMappings(sourceRepository.findAllElementMappings());
        sourceRegistry.initializeFieldMappings(sourceRepository.findAllFieldMappings());
    }

}
