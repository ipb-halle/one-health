package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.metadata.ElementType.ElementClass;
import de.ipb_halle.curator.metadata.FieldType.FieldTypeEnum;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Read-only repository for loading metadata from {@code element_types},
 * {@code field_types}, and {@code field_definitions} tables.
 */
@Repository
public class MetadataRepository {

    private final JdbcTemplate jdbcTemplate;

    public MetadataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Load all elements from the database.
     */
    @Transactional(readOnly = true)
    public List<ElementType> findAllElementTypes() {
        String sql = "SELECT id, element_class, label, name, description, ui_color FROM element_types";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ElementType(
                rs.getInt("id"),
                ElementClass.valueOf(rs.getString("element_class")),
                rs.getString("label"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getObject("ui_color", Integer.class)
        ));
    }

    /**
     * Load the field type enum from the database. Currently only one row expected.
     */
    @Transactional(readOnly = true)
    public List<FieldType> findAllFieldTypes() {
        String sql = "SELECT id, type, description, table_name FROM field_types ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new FieldType(
                rs.getInt("id"),
                FieldTypeEnum.valueOf(rs.getString("type")),
                rs.getString("description"),
                rs.getString("table_name")
        ));


    }

    /**
     * Load all field definitions from the database. Resolution of Elements
     * FieldTypes and FieldDefinitionDTOs is done during initialization of
     * the @MetadataRegistry.
     */
    @Transactional(readOnly = true)
    public List<FieldDefinition> findAllFieldDefinitions() {
        String sql = "SELECT id, field_type_id, element_type_id, name, description, "
                + "mandatory, multivalued FROM field_definitions fd";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new FieldDefinition(
                rs.getInt("id"),
                rs.getInt("field_type_id"),
                rs.getInt("element_type_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("mandatory"),
                rs.getBoolean("multivalued")
        ));
    }
}
