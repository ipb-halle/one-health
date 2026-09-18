/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.metadata.ElementType.ElementClass;
import java.util.HashMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

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
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("dyn_enums")
                .usingGeneratedKeyColumns("id")
                .usingColumns("field_id", "label", "description");

        Map<String, Object> params = new HashMap<> ();
        params.put("field_id", dynEnum.getFieldDefinitionId());
        params.put("label", dynEnum.getLabel());
        params.put("description", dynEnum.getDescription());
        Number id = insert.executeAndReturnKey(params);
        dynEnum.setId(id.intValue());
        return dynEnum;
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
