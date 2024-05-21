package ipbhalle.de.ontologymanagerserver.n4j.repository;

import ipbhalle.de.ontologymanagerserver.data.dtos.DataSourceDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeMappingDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.LinkTypeMappingDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IDataSourceRepository;
import ipbhalle.de.ontologymanagerserver.n4j.mapping.N4JMapper;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JDataSource;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JEntityType;
import ipbhalle.de.ontologymanagerserver.n4j.models.N4JLinkType;
import ipbhalle.de.ontologymanagerserver.services.interfaces.PageResult;
import ipbhalle.de.ontologymanagerserver.services.interfaces.QueryCommand;
import org.springframework.data.neo4j.core.Neo4jOperations;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.context.support.GroovyWebApplicationContext;

import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;


import java.util.List;

@Repository
public class N4JDataSourceRepository implements IDataSourceRepository {

    private Neo4jOperations neo4jOperations;

    public N4JDataSourceRepository(Neo4jOperations neo4jOperations) {
        this.neo4jOperations = neo4jOperations;
    }

    @Override
    public DataSourceDTO Create(DataSourceDTO dto) {
        var entity = N4JMapper.MAPPER.map(dto);
        entity = neo4jOperations.save(entity);
        return N4JMapper.MAPPER.map(entity);
    }

    @Override
    public List<DataSourceDTO> CreateAll(Iterable<DataSourceDTO> dtos) {
        return List.of();
    }

    @Override
    public DataSourceDTO Update(DataSourceDTO dto) {
        return null;
    }

    @Override
    public DataSourceDTO Get(String id) {
        var entity = neo4jOperations.findById(id, N4JDataSource.class);
        if (entity.isEmpty())
            throw new RuntimeException();

        return N4JMapper.MAPPER.map(entity.get());
    }

    @Override
    public List<DataSourceDTO> GetAll() {
        return neo4jOperations.findAll(N4JDataSource.class).stream().map(N4JMapper.MAPPER::map).toList();
    }

    @Override
    public void Delete(String id) {
        neo4jOperations.deleteById(id, N4JDataSource.class);
    }

    @Override
    public PageResult<DataSourceDTO> GetPage(QueryCommand query) {
        var result = GetAll();
        return new PageResult<>(result.size(), result);
    }


    @Override
    public String MapEntities(String id, EntityTypeMappingDTO mapping) {
        var entityType = neo4jOperations.findById(mapping.getType(), N4JEntityType.class);

        // Get the ancestors

        String ancestorsQuery =
                String.format("MATCH path = (ancestor:N4JEntityType)-[:PARENT_OF*0..]->(node:N4JEntityType) WHERE node.id = '%s' RETURN ancestor", mapping.getType());

        var ancestors = neo4jOperations.findAll(ancestorsQuery, N4JEntityType.class);

        String nodeTypes = ":" + String.join(":", ancestors.stream().map(x -> String.format("`%s`", x.getName())).toList()) + ":Entity";

        //  =====================

        StringBuilder result = new StringBuilder();


        var csvFile = "./src/main/resources/data/files/" + id + ".csv";

        int chunkSize = 1000; // Chunk size

        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            var header = csvParser.getHeaderMap();

            Iterable<CSVRecord> records = csvParser.getRecords();
            int count = 0;
            for (CSVRecord record : records) {
                // Process each record
                // Example: Print the record
                // TODO: there is a bug with parsing the first element of the header or something like that
                var props = mapping.getProperties().keySet().stream().map(k -> String.format("`%s`:'%s'", mapping.getProperties().get(k).getName(), record.get(header.get(k)))).toList();

                var entity = String.format("(%s {%s})", nodeTypes, String.join(",", props));

                result.append("MERGE ").append(entity);

                var x = header.get("Name");
                var y = header.get("Otra");





                count++;
                if (count % chunkSize == 0) {
                    System.out.println("Processed " + count + " records");
                    result.append("RETURN count(*)");
                    var query = result.toString();
                    neo4jOperations.count(query);
                    result = new StringBuilder();

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }






//        neo4jOperations.count(query);


        return "Done";


    }

    @Override
    public String MapLinks(String id, LinkTypeMappingDTO mapping) {
        var linkType = neo4jOperations.findById(mapping.getType(), N4JLinkType.class).get();

        StringBuilder matchClause = new StringBuilder().append("MATCH ");
        StringBuilder createClause = new StringBuilder().append("CREATE ");

        var csvFile = "./src/main/resources/data/files/" + id + ".csv";

        int chunkSize = 1000; // Chunk size

        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            var header = csvParser.getHeaderMap();

            Iterable<CSVRecord> records = csvParser.getRecords();
            int count = 0;
            for (CSVRecord record : records) {
                // Process each record
                // Example: Print the record

                var leftEntity = String.format(
                        "(n%d:`%s` {`%s`: '%s'})",
                        count,
                        linkType.getLeftEntityType().getName(),
                        mapping.getLeftEntityTypeKey(),
                        record.get(header.get( mapping.getLeftEntityTypeKeyMapping()))
                );

                var rightEntity = String.format(
                        "(n%d:`%s` {`%s`: '%s'})",
                        count + chunkSize + 1,
                        linkType.getRightEntityType().getName(),
                        mapping.getRightEntityTypeKey(),
                        record.get(header.get( mapping.getRightEntityTypeKeyMapping()))
                );

                matchClause.append(leftEntity).append(",").append(rightEntity).append(",");


                var link = String.format(
                        "(n%d) - [:`%s`] -> (n%d)",
                        count,
                        linkType.getName(),
                        count + chunkSize + 1
                );


                createClause.append(link).append(",");


                count++;
                if (count % chunkSize == 0) {
                    count = 0;
                    System.out.println("Processed " + count + " records");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        matchClause.deleteCharAt(matchClause.length() -1);
        createClause.deleteCharAt(createClause.length() - 1);


        var query = matchClause.toString() + createClause.toString();

//        neo4jOperations.count(query);


        return query;

    }
}
