package ipbhalle.de.ontologymanagerserver.postgre.repositories;

import ipbhalle.de.ontologymanagerserver.data.interfaces.IEntitySearchRepository;
import ipbhalle.de.ontologymanagerserver.postgre.mapping.PSQLMapper;
import ipbhalle.de.ontologymanagerserver.postgre.mapping.PSQLNaturalProductRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PSQLEntitySearchRepository implements IEntitySearchRepository {

    private final JdbcTemplate template;

    public PSQLEntitySearchRepository(JdbcTemplate template){
        this.template = template;
    }

    @Override
    public List<String> FindMatchingEntityIds(String query) {
        var sql = "select distinct entityid from entity_string_index where key ilike ?";

//        var result = template.queryForList(sql, String.class, query);
        try(var result = template.queryForStream(sql, (rs, rowNum) -> rs.getString("entityid"), "%" + query + "%")){
            return result.toList();
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
