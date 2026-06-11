package de.ipb_halle.server.postgre.mapping;

import org.springframework.jdbc.core.RowMapper;

import de.ipb_halle.server.postgre.models.PSQLNaturalProduct;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PSQLNaturalProductRowMapper implements RowMapper<PSQLNaturalProduct> {
    @Override
    public PSQLNaturalProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
        PSQLNaturalProduct naturalProduct = new PSQLNaturalProduct();
        naturalProduct.set__id(rs.getString("__id"));
        naturalProduct.setInchi(rs.getString("inchi"));
        naturalProduct.setInchikey(rs.getString("inchikey"));
        naturalProduct.setSmiles(rs.getString("smiles"));
        naturalProduct.setMolformula(rs.getString("molformula"));
        naturalProduct.setMolweight(rs.getDouble("molweight"));
        naturalProduct.setCas(rs.getString("cas"));
        naturalProduct.setIupac(rs.getString("iupac"));
        naturalProduct.setName(rs.getString("name"));
        return naturalProduct;
    }
}
