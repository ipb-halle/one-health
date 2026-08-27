package de.ipb_halle.curator.metadata;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Read-only repository for loading metadata from {@code node_types},
 * {@code field_types}, and {@code field_definitions} tables.
 */
@Repository
public class MetadataRepository {

    private final JdbcTemplate jdbcTemplate;

    public MetadataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Load all node types from the database.
     */
    @Transactional(readOnly = true)
    public List<NodeTypeInfo> findAllNodeTypes() {
        String sql = "SELECT id AS nt_id, name AS nt_name, graph_label AS nt_graph_label, description AS nt_description, ui_color AS nt_ui_color FROM node_types ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new NodeTypeInfo(
                rs.getInt("nt_id"),
                rs.getString("nt_name"),
                rs.getString("nt_graph_label"),
                rs.getString("nt_description"),
                rs.getObject("nt_ui_color", Integer.class)
        ));
    }

    /**
     * Load the field type enum from the database. Currently only one row expected.
     */
    @Transactional(readOnly = true)
    public List<FieldTypeEnum> findAllFieldTypes() {
        String sql = "SELECT name FROM field_types ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> FieldTypeEnum.fromName(rs.getString("name")));
    }

    /**
     * Load all field definitions from the database, resolving their type to an enum via join with field_types.
     */
    @Transactional(readOnly = true)
    public List<FieldDefinitionInfo> findAllFieldDefinitions() {
        String sql = "SELECT fd.id AS fd_id, fd.name AS fd_name, ft.name AS type_name, fd.description AS fd_description, fd.mandatory AS fd_mandatory, fd.multivalued AS fd_multivalued FROM field_definitions fd JOIN field_types ft ON fd.type = ft.id ORDER BY fd.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new FieldDefinitionInfo(
                rs.getInt("fd_id"),
                rs.getString("fd_name"),
                FieldTypeEnum.fromName(rs.getString("type_name")),
                rs.getString("fd_description"),
                rs.getBoolean("fd_mandatory"),
                rs.getBoolean("fd_multivalued")
        ));
    }

    /**
     * Find a single node type by its database ID.
     */
    @Transactional(readOnly = true)
    public Optional<NodeTypeInfo> findNodeTypeById(int id) {
        String sql = "SELECT id AS nt_id, name AS nt_name, graph_label AS nt_graph_label, description AS nt_description, ui_color AS nt_ui_color FROM node_types WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new NodeTypeInfo(
                rs.getInt("nt_id"),
                rs.getString("nt_name"),
                rs.getString("nt_graph_label"),
                rs.getString("nt_description"),
                rs.getObject("nt_ui_color", Integer.class)
        ), id).stream().findFirst();
    }

    /**
     * Find a single field definition by its database ID, joining with field_types for the enum name.
     */
    @Transactional(readOnly = true)
    public Optional<FieldDefinitionInfo> findFieldDefinitionById(int id) {
        String sql = "SELECT fd.id AS fd_id, fd.name AS fd_name, ft.name AS fd_type, fd.description AS fd_desc, fd.mandatory AS fd_mand, fd.multivalued AS fd_multi FROM field_definitions fd JOIN field_types ft ON fd.type = ft.id WHERE fd.id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new FieldDefinitionInfo(
                rs.getInt("fd_id"),
                rs.getString("fd_name"),
                FieldTypeEnum.fromName(rs.getString("fd_type")),
                rs.getString("fd_desc"),
                rs.getBoolean("fd_mand"),
                rs.getBoolean("fd_multi")
        ), id).stream().findFirst();
    }
}
