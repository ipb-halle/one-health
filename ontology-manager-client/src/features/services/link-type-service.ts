import { PagedCrudService } from "./paged-crud-service";
import { IHttpResponseHandlerSettings } from "./http-responses-handler";
import { injectable } from "inversify";
import { ILinkType } from "../ontology/link-types";

@injectable()
export class ILinkTypeService extends PagedCrudService<ILinkType>{};


@injectable()
export class LinkTypeService extends ILinkTypeService {
    url: string = "api/link-type"
    entityTitle: string = "Link Type";

    // get(id: number | string, httpResponseHandlerSettings? : IHttpResponseHandlerSettings): string {
    //     return "Link Type";
    // };
}









