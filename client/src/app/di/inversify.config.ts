// InversifyJS dependency injection container configuration

import { Container } from 'inversify';

import {
    IEntityTypeService,
    EntityTypeService,
} from '../../features/metadata/services/entity-type-service';

import {
    IPropertyService,
    PropertyService,
} from '../../features/metadata/services/property-service';

import {
    IEntityService,
    EntityService,
} from '../../features/visualization/neighborhood-explorer/services/entity-service';

import {
    IOntologyService,
    OntologyService,
} from '../../features/visualization/co-ocurrence-search/services/visualization-service';

import {
    IGraphVisualizationHistoryService,
    MockGraphVisualizationHistoryService,
} from '../../features/visualization/neighborhood-explorer/services/graph-visualization-history-service';

import {
    ICompoundService,
    CompoundService,
} from '../../features/search/compound-search/services/compound-search-service';

import {
    IGeneralSearchService,
    GeneralSearchService,
} from '../../features/search/general-search/services/general-search-service';

import {
    IGeneralSearchHistoryService,
    MockGeneralSearchHistoryService,
} from '../../features/search/search-history/services/general-search-history-service';

import type { MessageService } from '../../core/api/messages/interfaces/message-service';

import {
    ToastMessageService,
} from '../../app/providers/messages/toast-message-service';

import { SERVICES } from '@/app/di/service-types';

import {
    ICoOcurrenceVisualizationHistoryService,
    MockCoOcurrenceVisualizationHistoryService,
} from '../../features/visualization/co-ocurrence-search/services/co-ocurrence-visualization-history-service';

import {
    ILocalStorageStore,
    ITutorialStore,
    LocalStorageStore,
    STORES,
    TutorialStore,
} from '../../store/inversify';

import {
    INeighborhoodExplorerStore,
    NeighborhoodExplorerStore,
} from '../../store/inversify/neighborhood-explorer-store';

const dependencyFactory = new Container();

dependencyFactory
    .bind<IEntityTypeService>(SERVICES.IEntityTypeService)
    .to(EntityTypeService);

dependencyFactory
    .bind<IPropertyService>(SERVICES.IPropertyService)
    .to(PropertyService);

dependencyFactory
    .bind<IOntologyService>(SERVICES.IOntologyService)
    .to(OntologyService);

dependencyFactory
    .bind<IEntityService>(SERVICES.IEntityService)
    .to(EntityService);

dependencyFactory
    .bind<IGraphVisualizationHistoryService>(
        SERVICES.IGraphVisualizationHistoryService,
    )
    .to(MockGraphVisualizationHistoryService)
    .inSingletonScope();

dependencyFactory
    .bind<ICoOcurrenceVisualizationHistoryService>(
        SERVICES.ICoOcurrenceVisualizationHistoryService,
    )
    .to(MockCoOcurrenceVisualizationHistoryService)
    .inSingletonScope();

dependencyFactory
    .bind<ITutorialStore>(STORES.ITutorialStore)
    .to(TutorialStore)
    .inSingletonScope();

dependencyFactory
    .bind<ICompoundService>(SERVICES.ICompoundService)
    .to(CompoundService);

dependencyFactory
    .bind<IGeneralSearchService>(SERVICES.IGeneralSearchService)
    .to(GeneralSearchService);

dependencyFactory
    .bind<IGeneralSearchHistoryService>(SERVICES.IGeneralSearchHistoryService)
    .to(MockGeneralSearchHistoryService)
    .inSingletonScope();

dependencyFactory
    .bind<INeighborhoodExplorerStore>(STORES.INeighborhoodExplorerStore)
    .to(NeighborhoodExplorerStore)
    .inSingletonScope();

dependencyFactory
    .bind<ILocalStorageStore>(STORES.ILocalStorageStore)
    .to(LocalStorageStore);

dependencyFactory
    .bind<MessageService>(SERVICES.MessageService)
    .to(ToastMessageService);

export { dependencyFactory };