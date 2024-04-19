package ipbhalle.de.ontologymanagerserver.services;

import ipbhalle.de.ontologymanagerserver.data.dtos.DataSourceDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.EntityTypeMappingDTO;
import ipbhalle.de.ontologymanagerserver.data.dtos.LinkTypeMappingDTO;
import ipbhalle.de.ontologymanagerserver.data.interfaces.IDataSourceRepository;
import ipbhalle.de.ontologymanagerserver.services.interfaces.IDataSourceService;
import ipbhalle.de.ontologymanagerserver.services.interfaces.PageResult;
import ipbhalle.de.ontologymanagerserver.services.interfaces.QueryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class DataSourceService implements IDataSourceService {

    private final String path = "./src/main/resources/data/files";
    private IDataSourceRepository dataSourceRepository;

    @Autowired
    public DataSourceService(IDataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public DataSourceDTO Create(DataSourceDTO dto) {return dataSourceRepository.Create(dto);
    }

    @Override
    public DataSourceDTO Update(DataSourceDTO dto) {
        return null;
    }

    @Override
    public DataSourceDTO Get(String id) {
        return dataSourceRepository.Get(id);
    }

    @Override
    public List<DataSourceDTO> GetAll() {
        return dataSourceRepository.GetAll();
    }

    @Override
    public void Delete(String id) {
        dataSourceRepository.Delete(id);
    }

    @Override
    public PageResult<DataSourceDTO> GetPage(QueryCommand queryCommand) {
        return dataSourceRepository.GetPage(queryCommand);
    }


    @Override
    public void WriteFile(String fileId, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.csv", path, fileId), true));
            writer.write(content);
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String MapEntities(String id, EntityTypeMappingDTO mapping) {
        return dataSourceRepository.MapEntities(id, mapping);
    }

    @Override
    public String MapLinks(String id, LinkTypeMappingDTO mapping) {
        return dataSourceRepository.MapLinks(id, mapping);
    }
}

