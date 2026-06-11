package de.ipb_halle.server.api;

import org.springframework.web.bind.annotation.*;

import de.ipb_halle.server.api.interfaces.PagedDataController;
import de.ipb_halle.server.data.dtos.DataSourceDTO;
import de.ipb_halle.server.data.dtos.EntityTypeMappingDTO;
import de.ipb_halle.server.data.dtos.LinkTypeMappingDTO;
import de.ipb_halle.server.services.interfaces.IDataSourceService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="api/data-source")
public class DataSourceController extends PagedDataController<DataSourceDTO, DataSourceDTO, String> {

    private final IDataSourceService dataSourceService;
    public DataSourceController(IDataSourceService crudHandler) {

        super(crudHandler);
        this.dataSourceService = crudHandler;
    }


    @PostMapping("write-file/{id}")
    public String UploadFile(@PathVariable String id, @RequestBody String fileChunk){
        dataSourceService.WriteFile(id, fileChunk);
        return "hello";
    }


    @PostMapping("map-entities/{id}")
    public String MapDataSource(@PathVariable String id, @RequestBody EntityTypeMappingDTO mapping){
        return dataSourceService.MapEntities(id, mapping);
    }

    @PostMapping("map-links/{id}")
    public String MapDataSource(@PathVariable String id, @RequestBody LinkTypeMappingDTO mapping){
        return dataSourceService.MapLinks(id, mapping);
    }


}
