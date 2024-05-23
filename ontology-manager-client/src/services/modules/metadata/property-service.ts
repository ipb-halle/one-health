import { PagedCrudService } from "../../interfaces/paged-crud-service";
import { IHttpResponseHandlerSettings } from "../../../features/shared/http/http-responses-handler";
import { injectable } from "inversify";
import { IProperty } from "../../../features/modules/metadata/properties";

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