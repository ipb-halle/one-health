/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.metadata.ElementType.ElementClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Read-only repository for loading metadata from {@code element_types},
 *  {@code field_definitions}, and  {@code dyn_enums} tables.
 */
@Repository
public class MetadataRepository {

    private final JdbcTemplate jdbcTemplate;

    public MetadataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<DynEnum> findAllDynEnums() {
        String sql = "SELECT id, field_id, label, description FROM dyn_enums";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new DynEnum(
                rs.getInt("id"),
                rs.getInt("field_id"),
                rs.getString("label"),
                rs.getString("description")
        ));
    }

    @Transactional(readOnly = false)
    public DynEnum save(DynEnum dynEnum) {
        String sql = "INSERT INTO dyn_enums (field_id, label, description) VALUES (?,?,?) RETURNING id AS id";
        return jdbcTemplate.query(sql, (rs, rownum) -> new DynEnum(
                rs.getInt("id"),
                dynEnum.getFieldDefinitionId(),
                dynEnum.getLabel(),
                dynEnum.getDescription())
        ).get(0);
    }

    /**
     * Load all elements from the database.
     */
    @Transactional(readOnly = true)
    public List<ElementType> findAllElementTypes() {
        String sql = "SELECT id, element_class, name, description, ui_color FROM element_types";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ElementType(
                rs.getString("id"),
                ElementClass.valueOf(rs.getString("element_class")),
                rs.getString("name"),
                rs.getString("description"),
                rs.getObject("ui_color", Integer.class)
        ));
    }

    /**
     * Load all field definitions from the database. Resolution of Elements
     * FieldTypes and FieldDefinitionDTOs is done during initialization of
     * the @MetadataRegistry.
     */
    @Transactional(readOnly = true)
    public List<FieldDefinition> findAllFieldDefinitions() {
        String sql = "SELECT id, field_type, element_type_id, graph_export_order, "
                + "name, description, mandatory, multivalued "
                + "FROM field_definitions fd";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new FieldDefinition(
                rs.getInt("id"),
                FieldType.valueOf(rs.getString("field_type")),
                rs.getString("element_type_id"),
                rs.getObject("graph_export_order", Integer.class),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("mandatory"),
                rs.getBoolean("multivalued")
        ));
    }
}
