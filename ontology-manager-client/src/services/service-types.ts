// This file contains the symbols used for dependency injection
const SERVICES = {
    IOntologyService: Symbol.for("IOntologyService"),
    IMetadataService: Symbol.for("IMetadataService"),
    IKeywordService: Symbol.for("IKeywordService"),
    IEntityTypeService : Symbol.for("IEntityTypeService"),
    ILinkTypeService: Symbol.for("ILinkTypeService"),
    IPropertyService: Symbol.for("IPropertyService"),
    IDataSourceService: Symbol.for("IDataSourceService"),
    IEntityService: Symbol.for("IEntityService"),
    IGraphVisualizationHistoryService: Symbol.for("IGraphVisualizationHistoryService"),
    ICoOcurrenceVisualizationHistoryService: Symbol.for("ICoOcurrenceVisualizationHistoryService")
}

export {SERVICES};