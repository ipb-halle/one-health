// This file contains the service symbols used for dependency injection
const SERVICES = {
    IOntologyService: Symbol.for('IOntologyService'),
    IEntityTypeService: Symbol.for('IEntityTypeService'),
    IPropertyService: Symbol.for('IPropertyService'),
    IEntityService: Symbol.for('IEntityService'),
    IGraphVisualizationHistoryService: Symbol.for(
        'IGraphVisualizationHistoryService',
    ),
    ICoOcurrenceVisualizationHistoryService: Symbol.for(
        'ICoOcurrenceVisualizationHistoryService',
    ),
    ICompoundService: Symbol.for('ICompoundService'),
    IGeneralSearchService: Symbol.for('IGeneralSearchService'),
    IGeneralSearchHistoryService: Symbol.for('IGeneralSearchHistoryService'),
    MessageService: Symbol.for('MessageService'),
};

export { SERVICES };
