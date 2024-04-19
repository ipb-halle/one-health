import { PagedCrudService } from "./paged-crud-service";
import { IHttpResponseHandlerSettings } from "./http-responses-handler";
import { injectable } from "inversify";
import { IProperty } from "../ontology/properties";

@injectable()
export class IPropertyService extends PagedCrudService<IProperty>{};


@injectable()
export class PropertyService extends IPropertyService {
    url: string = "/property/"
    entityTitle: string = "Property";

    // get(id: number | string, httpResponseHandlerSettings? : IHttpResponseHandlerSettings): string {
    //     return "Property";
    // };
}