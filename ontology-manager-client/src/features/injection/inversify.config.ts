import { Container } from "inversify";
import { SERVICE_TYPES } from "../services/service-types";
import { IEntityTypeService, ILinkTypeService, IPropertyService, IDataSourceService } from "../services";
import { EntityTypeService } from "../services/entity-type-service";
import { LinkTypeService } from "../services/link-type-service";
import { PropertyService } from "../services/property-service";
import { DataSourceService } from "../services/data-source-service";
import { IKeywordService, KeywordService } from "../services/keyword-service";
import { IMetadataService, MetadataService } from "../services/metadata-service";
import { IOntologyService, OntologyService } from "../services/visualization-service";
import { EntityService, IEntityService } from "../services/entity-service";


const dependencyFactory = new Container();
dependencyFactory.bind<IEntityTypeService>(SERVICE_TYPES.IEntityTypeService).to(EntityTypeService);
dependencyFactory.bind<IKeywordService>(SERVICE_TYPES.IKeywordService).to(KeywordService);
dependencyFactory.bind<ILinkTypeService>(SERVICE_TYPES.ILinkTypeService).to(LinkTypeService);
dependencyFactory.bind<IPropertyService>(SERVICE_TYPES.IPropertyService).to(PropertyService);
dependencyFactory.bind<IDataSourceService>(SERVICE_TYPES.IDataSourceService).to(DataSourceService);
dependencyFactory.bind<IMetadataService>(SERVICE_TYPES.IMetadataService).to(MetadataService);
dependencyFactory.bind<IOntologyService>(SERVICE_TYPES.IOntologyService).to(OntologyService);
dependencyFactory.bind<IEntityService>(SERVICE_TYPES.IEntityService).to(EntityService);


export { dependencyFactory };