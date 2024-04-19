import { IEntityType } from "../ontology/entity-types";
import { IHttpResponseHandlerSettings } from "./http-responses-handler";
import { injectable } from "inversify";
import { PagedCrudService } from "./paged-crud-service";
import { SelectableOption } from "../utils/selectable-option";
import { RefObject } from "react";
import { Toast } from "primereact/toast";
import axios, { Axios } from "axios";



// TODO: Wrap toast inside an interface IMessager or something like that

@injectable()
export class IEntityTypeService extends PagedCrudService<IEntityType>{
    getAllEntityTypesAsOptions(toast : RefObject<Toast>, httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : Promise<SelectableOption[]> {
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









