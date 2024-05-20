import { ILinkType } from "../../../features/modules/metadata/link-types";
import { PagedCrudService } from "../../interfaces/paged-crud-service";
import { injectable } from "inversify";

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









