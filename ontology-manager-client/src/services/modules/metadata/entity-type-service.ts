
import { IHttpResponseHandlerSettings } from "../../../features/shared/http/http-responses-handler";
import { injectable } from "inversify";
import { PagedCrudService } from "../../interfaces/paged-crud-service";
import { SelectableOption } from "../../../utils/selectable-option";
import axios from "axios";
import { IEntityType } from "../../../features/modules/metadata/entity-types";
import { MessageService } from "../../../features/shared/messages";

@injectable()
export class IEntityTypeService extends PagedCrudService<IEntityType>{
    getAllEntityTypesAsOptions(messageService: MessageService, httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : Promise<SelectableOption[]> {
        return this.handleRequest<SelectableOption[]>(
            axios.get<SelectableOption[]>(`${this.url}/as-options`)
        ).then(x => x).catch(x => x);
    };
};


@injectable()
export class EntityTypeService extends IEntityTypeService {
    url: string = "api/entity-type"
    entityTitle: string = "Entity Type";
}









