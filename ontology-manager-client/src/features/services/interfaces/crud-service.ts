import { injectable } from "inversify";
import { IEntityType } from "../../ontology/entity-types/entity-type";
import { BaseDataService } from "./base-data-service";
import { BaseHttpResponsesHandler, IHttpResponseHandlerSettings, OnReadByIdResponsesHandler } from "../utils/http-responses-handler";
import axios, { Axios } from "axios";
import { Toast } from "primereact/toast";
import { RefObject } from "react";
import { MessageService } from "../../messages";


/**
* Provides base implementations of the standard CRUD operations
* @extends BaseDataService
*/
@injectable()
export class CrudService<TEntity> extends BaseDataService {
    readonly entityTitle: string = "";

    get(id: number | string, messageService: MessageService, httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<TEntity> {

        //TODO:how to fix the catch here
        return this.handleRequest<TEntity>(
            axios.get<TEntity>(`${this.url}/${id}`),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    };

    getAll(messageService: MessageService, httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<TEntity[]> {
        return this.handleRequest<TEntity[]>(
            axios.get<TEntity[]>(`${this.url}/all`),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    };

    create(item: TEntity, messageService: MessageService, httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<TEntity> {
        return this.handleRequest<TEntity>(
            axios.post<TEntity>(`${this.url}`, item),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    };
    update(id: number | string, item: TEntity, httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : Promise<TEntity> {
        throw new Error();
    };
    delete(id: number | string, httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : Promise<any> {
        throw new Error();
    };
    deleteAll(httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        throw new Error();
    };
}



