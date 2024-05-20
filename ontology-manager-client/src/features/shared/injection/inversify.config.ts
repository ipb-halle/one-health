import { Container } from "inversify";
import { 
    DataSourceService, 
    EntityService, 
    EntityTypeService, 
    IDataSourceService, 
    IEntityService, 
    IEntityTypeService, 
    IKeywordService, 
    ILinkTypeService, 
    IMetadataService, 
    IOntologyService, 
    IPropertyService, 
    KeywordService, 
    LinkTypeService, 
    MetadataService, 
    OntologyService, 
    PropertyService,
    IGraphVisualizationHistoryService, 
    MockGraphVisualizationHistoryService,
    SERVICES 
} from "../../../services";



const dependencyFactory = new Container();
dependencyFactory.bind<IEntityTypeService>(SERVICES.IEntityTypeService).to(EntityTypeService);
dependencyFactory.bind<IKeywordService>(SERVICES.IKeywordService).to(KeywordService);
dependencyFactory.bind<ILinkTypeService>(SERVICES.ILinkTypeService).to(LinkTypeService);
dependencyFactory.bind<IPropertyService>(SERVICES.IPropertyService).to(PropertyService);
dependencyFactory.bind<IDataSourceService>(SERVICES.IDataSourceService).to(DataSourceService);
dependencyFactory.bind<IMetadataService>(SERVICES.IMetadataService).to(MetadataService);
dependencyFactory.bind<IOntologyService>(SERVICES.IOntologyService).to(OntologyService);
dependencyFactory.bind<IEntityService>(SERVICES.IEntityService).to(EntityService);
dependencyFactory.bind<IGraphVisualizationHistoryService>(SERVICES.IGraphVisualizationHistoryService).to(MockGraphVisualizationHistoryService).inSingletonScope();


export {dependencyFactory};