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
    SERVICES, 
    ICompoundService,
    CompoundService,
    IGeneralSearchService,
    GeneralSearchService,
    IGeneralSearchHistoryService,
    MockGeneralSearchHistoryService
} from "../../../services";
import { ICoOcurrenceVisualizationHistoryService, MockCoOcurrenceVisualizationHistoryService } from "../../../services/modules/visualization/co-ocurrence-visualization-history-service";
import { ITutorialStore, STORES, TutorialStore } from "../../../stores";
import { INeighborhoodExplorerStore, NeighborhoodExplorerStore } from "../../../stores/neighborhood-explorer-store";



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
dependencyFactory.bind<ICoOcurrenceVisualizationHistoryService>(SERVICES.ICoOcurrenceVisualizationHistoryService).to(MockCoOcurrenceVisualizationHistoryService).inSingletonScope();
dependencyFactory.bind<ITutorialStore>(STORES.ITutorialStore).to(TutorialStore).inSingletonScope();
dependencyFactory.bind<ICompoundService>(SERVICES.ICompoundService).to(CompoundService);
dependencyFactory.bind<IGeneralSearchService>(SERVICES.IGeneralSearchService).to(GeneralSearchService);
dependencyFactory.bind<IGeneralSearchHistoryService>(SERVICES.IGeneralSearchHistoryService).to(MockGeneralSearchHistoryService).inSingletonScope();
dependencyFactory.bind<INeighborhoodExplorerStore>(STORES.INeighborhoodExplorerStore).to(NeighborhoodExplorerStore).inSingletonScope();

export {dependencyFactory};