// This file contains the symbols used for dependency injection
const SERVICE_TYPES = {
    IOntologyService: Symbol.for("IOntologyService"),
    IMetadataService: Symbol.for("IMetadataService"),
    IKeywordService: Symbol.for("IKeywordService"),
    IEntityTypeService : Symbol.for("IEntityTypeService"),
    ILinkTypeService: Symbol.for("ILinkTypeService"),
    IPropertyService: Symbol.for("IPropertyService"),
    IDataSourceService: Symbol.for("IDataSourceService"),
}

export {SERVICE_TYPES};