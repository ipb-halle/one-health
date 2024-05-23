package ipbhalle.de.ontologymanagerserver.n4j.repository;

import ipbhalle.de.ontologymanagerserver.data.dtos.KeywordDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IKeywordRepository;
import ipbhalle.de.ontologymanagerserver.n4j.mapping.N4JMapper;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JKeyword;
import org.neo4j.cypherdsl.core.Cypher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class N4JKeywordRepository implements IKeywordRepository {

    private final Neo4jTemplate neo4jTemplate;

    public N4JKeywordRepository(Neo4jTemplate neo4jTemplate) {
        this.neo4jTemplate = neo4jTemplate;
    }

    @Override
    public KeywordDTO Create(KeywordDTO dto) {
        N4JKeyword entity = N4JMapper.MAPPER.map(dto);
        entity = neo4jTemplate.save(entity);
        return N4JMapper.MAPPER.map(entity);
    }

    @Override
    public List<KeywordDTO> CreateAll(Iterable<KeywordDTO> dtos) {
        return List.of();
    }

    @Override
    public KeywordDTO Update(KeywordDTO dto) {
        return null;
    }

    @Override
    public KeywordDTO Get(String id) {
        return null;
    }

    @Override
    public List<KeywordDTO> GetAll() {
        return neo4jTemplate.findAll(N4JKeyword.class).stream().map(N4JMapper.MAPPER::map).toList();
    }

    @Override
    public void Delete(String id) {
        Optional<N4JKeyword> entity = neo4jTemplate.findById(id, N4JKeyword.class);
        if (entity.isEmpty())
            throw  new RuntimeException(); // TODO: add custom exception

        neo4jTemplate.deleteById(entity.get().getId(), N4JKeyword.class);
    }


    @Override
    public KeywordDTO GetByValue(String value) {
        String query = String.format("MATCH (k: N4JKeyword { value: '%s' }) RETURN k", value);
        Optional<N4JKeyword> result = neo4jTemplate.findAll(query, N4JKeyword.class).stream().findFirst();
        return result.isPresent() ? N4JMapper.MAPPER.map(result.get()) : null;
    }
}
