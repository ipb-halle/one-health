package de.ipb_halle.server.postgre.repositories;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import de.ipb_halle.server.data.dtos.NaturalProductDTO;
import de.ipb_halle.server.data.interfaces.INaturalProductRepository;
import de.ipb_halle.server.postgre.mapping.PSQLMapper;
import de.ipb_halle.server.postgre.mapping.PSQLNaturalProductRowMapper;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PSQLNaturalProductRepository implements INaturalProductRepository {

    @Autowired
    private JdbcTemplate template;

    @Override
    public NaturalProductDTO GetBySMILES(String value) {
        var query = """
            select __id::text, inchi, inchikey, smiles, molformula, ROUND(molweight::numeric, 2) as molweight, cas,iupac, name 
            from compounds_index 
            where canonical_smiles = mol_to_smiles(mol_from_smiles(?))::text limit 1""";
        return QueryForCompound(query, value);
    }

    @Override
    public NaturalProductDTO GetByInChI(String value) {
        var query = """
                select __id::text, inchi, inchikey, smiles, molformula, ROUND(molweight::numeric, 2) as molweight, cas,iupac, name 
                from compounds_index where inchi = ? limit 1""";
        return QueryForCompound(query, value);
    }

    @Override
    public NaturalProductDTO GetByInChIKey(String value) {
        var query = "select __id::text, inchi, inchikey, smiles, molformula, ROUND(molweight::numeric, 2) as molweight, cas,iupac, name from compounds_index where inchikey = ? limit 1";
        return QueryForCompound(query, value);
    }

    @Override
    public List<NaturalProductDTO> GetBySubstructure(String smiles, int take, int page) {
        var query = "select __id::text, inchi, inchikey, smiles, molformula, ROUND(molweight::numeric, 2) as molweight, cas,iupac, name from compounds_index ci  where m@> ? order by molweight offset ? limit ? ;";
        return QueryForCompoundList(query, smiles, take * page, take);
    }

    @Override
    public List<NaturalProductDTO> GetBySimilarity(String smiles, int threshold, int limit) {
        var query = """
                select __id::text, inchi, inchikey, smiles, molformula, ROUND(molweight::numeric, 2) as molweight, cas,iupac, name
                        FROM compounds_index ci
                        WHERE morganbv_fp(m) % morganbv_fp(mol_from_smiles(?)) and tanimoto_sml(morgan_fp, morganbv_fp(mol_from_smiles(?))) >= ?
                        ORDER BY tanimoto_sml(morgan_fp, morganbv_fp(mol_from_smiles(?))) desc
                        LIMIT ?;
        """;
        double t = (double) threshold / 100;
        return QueryForCompoundList(query, smiles, smiles,  t, smiles, limit);

    }

    private NaturalProductDTO QueryForCompound(String query, @Nullable Object... args) {
        try (var result =  template.queryForStream(query, new PSQLNaturalProductRowMapper(), args)){
            var first = result.findFirst();
            if (first.isEmpty())
                return null;
            return PSQLMapper.MAPPER.map(first.get());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<NaturalProductDTO> QueryForCompoundList(String query, @Nullable Object... args){
        try (var result =  template.queryForStream(query, new PSQLNaturalProductRowMapper(), args)){
            return  result.map(PSQLMapper.MAPPER::map).toList();
        } catch ( Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
