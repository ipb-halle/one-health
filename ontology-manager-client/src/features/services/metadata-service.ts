import { PagedCrudService } from "./paged-crud-service";
import { IHttpResponseHandlerSettings } from "./http-responses-handler";
import { injectable } from "inversify";
import { ILinkType } from "../ontology/link-types";
import { BaseDataService } from "./base-data-service";
import axios from "axios";
import { RefObject } from "react";
import { Toast } from "primereact/toast";
import { OnReadByIdResponsesHandler } from "./http-responses-handler";
import { SelectableOption } from "../utils/selectable-option";

@injectable()
export class IMetadataService extends BaseDataService{
    url: string = "api/metadata"
    entityTitle: string = "Metadata";

    getAll(toast : RefObject<Toast>,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/all`),
            new OnReadByIdResponsesHandler(this.entityTitle, toast, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    };

    getEntityType(id: string, toast : RefObject<Toast>,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/entity-type/${id}`),
            new OnReadByIdResponsesHandler(this.entityTitle, toast, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    };

    getLinkType(id: string, toast : RefObject<Toast>,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/link-type/${id}`),
            new OnReadByIdResponsesHandler(this.entityTitle, toast, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    };

    getSummary( toast : RefObject<Toast>,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/summary`),
            new OnReadByIdResponsesHandler(this.entityTitle, toast, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    };

    getAllAsOptions(toast : RefObject<Toast>, httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : Promise<SelectableOption[]> {
        return this.handleRequest<SelectableOption[]>(
            axios.get<SelectableOption[]>(`${this.url}/as-options`)
        ).then(x => x).catch(x => x);
    };


};


@injectable()
export class MetadataService extends IMetadataService {
  


   
}