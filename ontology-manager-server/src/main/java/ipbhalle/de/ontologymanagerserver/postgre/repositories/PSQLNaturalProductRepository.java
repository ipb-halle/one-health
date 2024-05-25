package ipbhalle.de.ontologymanagerserver.postgre.repositories;

import ipbhalle.de.ontologymanagerserver.data.dtos.NaturalProductDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.INaturalProductRepository;
import ipbhalle.de.ontologymanagerserver.data.query.NaturalProductStructureQuery;
import ipbhalle.de.ontologymanagerserver.postgre.mapping.PSQLMapper;
import ipbhalle.de.ontologymanagerserver.postgre.mapping.PSQLNaturalProductRowMapper;
import ipbhalle.de.ontologymanagerserver.postgre.models.PSQLNaturalProduct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PSQLNaturalProductRepository implements INaturalProductRepository {

    private final JdbcTemplate template;

    public PSQLNaturalProductRepository(JdbcTemplate template){
        this.template = template;
    }

    @Override
    public NaturalProductDTO GetBySMILES(String value) {
        var query = """
            select __id::text, inchi, inchikey, smiles, molformula, molweight, cas,iupac, name 
            from compounds_index 
            where canonical_smiles = mol_to_smiles(mol_from_smiles(?))::text limit 1""";
        var result =  template.queryForObject(query, new PSQLNaturalProductRowMapper(), value);
        return PSQLMapper.MAPPER.map(result);
    }

    @Override
    public NaturalProductDTO GetByInChI(String value) {
        var query = """
                select __id::text, inchi, inchikey, smiles, molformula, molweight, cas,iupac, name 
                from compounds_index where inchi = ? limit 1""";

        var result =  template.queryForObject(query, new PSQLNaturalProductRowMapper(), value);

        return PSQLMapper.MAPPER.map(result);
    }

    @Override
    public NaturalProductDTO GetByInChIKey(String value) {
        var query = "select __id::text, inchi, inchikey, smiles, molformula, molweight, cas,iupac, name from compounds_index where inchikey = ? limit 1";
        var result =  template.queryForObject(query, new PSQLNaturalProductRowMapper(), value);

        return PSQLMapper.MAPPER.map(result);
    }

    @Override
    public List<NaturalProductDTO> GetBySubstructure(String smiles, int take, int page) {
        var query = "select __id::text, inchi, inchikey, smiles, molformula, molweight, cas,iupac, name from compounds_index ci  where m@> ? offset ? limit ?;";
        var result =  template.queryForStream(query, new PSQLNaturalProductRowMapper(), smiles, take * page, take);

        return  result.map(PSQLMapper.MAPPER::map).toList();
    }

    @Override
    public List<NaturalProductDTO> GetBySimilarity(String smiles, int threshold, int limit) {
        var query = """
                select __id::text, inchi, inchikey, smiles, molformula, molweight, cas,iupac, name
                        FROM compounds_index ci
                        WHERE morganbv_fp(m) % morganbv_fp(mol_from_smiles(?)) and tanimoto_sml(morgan_fp, morganbv_fp(mol_from_smiles(?))) >= ?
                        ORDER BY tanimoto_sml(morgan_fp, morganbv_fp(mol_from_smiles(?))) desc
                        LIMIT ?;
        """;
        double t = (double) threshold / 100;
        var result =  template.queryForStream(query, new PSQLNaturalProductRowMapper(), smiles, smiles,  t, smiles, limit);
        return  result.map(PSQLMapper.MAPPER::map).toList();
    }

}
