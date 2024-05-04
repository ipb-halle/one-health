import { IEntityType } from "../ontology/entity-types";
import { IHttpResponseHandlerSettings } from "./utils/http-responses-handler";
import { injectable } from "inversify";
import { PagedCrudService } from "./interfaces/paged-crud-service";
import { SelectableOption } from "../utils/selectable-option";
import axios from "axios";
import { MessageService } from "../messages";

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









