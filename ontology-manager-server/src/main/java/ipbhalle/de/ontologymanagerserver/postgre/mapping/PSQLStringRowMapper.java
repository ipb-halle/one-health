package ipbhalle.de.ontologymanagerserver.postgre.mapping;

import ipbhalle.de.ontologymanagerserver.postgre.models.PSQLNaturalProduct;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PSQLStringRowMapper implements RowMapper<String> {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("entityid");
    }
}
