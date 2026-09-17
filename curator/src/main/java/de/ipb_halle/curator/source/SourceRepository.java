/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.source;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Read-only repository for loading source metadata from {@code data_sources},
 *  {@code element_mappings}, and  {@code field_mappings} tables.
 */
@Repository
public class SourceRepository {

    private final JdbcTemplate jdbcTemplate;

    public SourceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<DataSourceEntity> findAllDataSources() {
        String sql = "SELECT id, description, handler, source_url FROM data_sources ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new DataSourceEntity(
                rs.getString("id"),
                rs.getString("description"),
                rs.getString("handler"),
                rs.getString("source_url")
        ));
    }

    @Transactional(readOnly = true)
    public List<ElementMappingEntity> findAllElementMappings() {
        String sql = "SELECT id, data_source_id, element_type_id, source_field_name, identity_mapping, multivalued "
                + "FROM element_mappings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ElementMappingEntity(
                rs.getInt("id"),
                rs.getString("data_source_id"),
                rs.getString("element_type_id"),
                rs.getString("source_field_name"),
                rs.getBoolean("multivalued"),
                rs.getString("identity_mapping")
        ));
    }

    @Transactional(readOnly = true)
    public List<FieldMappingEntity> findAllFieldMappings() {
        String sql = "SELECT id, data_source_id,  source_field_name, mapping, multivalued "
                + "FROM field_mappings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new FieldMappingEntity(
                rs.getInt("id"),
                rs.getString("data_source_id"),
                rs.getString("source_field_name"),
                rs.getString("mapping"),
                rs.getBoolean("multivalued")
        ));
    }
}
