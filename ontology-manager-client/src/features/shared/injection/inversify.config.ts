import { Container } from "inversify";
import { DataSourceService, EntityService, EntityTypeService, IDataSourceService, IEntityService, IEntityTypeService, IKeywordService, ILinkTypeService, IMetadataService, IOntologyService, IPropertyService, KeywordService, LinkTypeService, MetadataService, OntologyService, PropertyService, SERVICES } from "../../../services";
import { IQueryHistoryGraphService, QueryHistoryGraphService } from "../../query-history/query-history-graph/query-history-graph.service";


const dependencyFactory = new Container();
dependencyFactory.bind<IEntityTypeService>(SERVICES.IEntityTypeService).to(EntityTypeService);
dependencyFactory.bind<IKeywordService>(SERVICES.IKeywordService).to(KeywordService);
dependencyFactory.bind<ILinkTypeService>(SERVICES.ILinkTypeService).to(LinkTypeService);
dependencyFactory.bind<IPropertyService>(SERVICES.IPropertyService).to(PropertyService);
dependencyFactory.bind<IDataSourceService>(SERVICES.IDataSourceService).to(DataSourceService);
dependencyFactory.bind<IMetadataService>(SERVICES.IMetadataService).to(MetadataService);
dependencyFactory.bind<IOntologyService>(SERVICES.IOntologyService).to(OntologyService);
dependencyFactory.bind<IEntityService>(SERVICES.IEntityService).to(EntityService);
dependencyFactory.bind<IQueryHistoryGraphService>(SERVICES.IQueryHistoryGraphService).to(QueryHistoryGraphService).inSingletonScope();


export {dependencyFactory};