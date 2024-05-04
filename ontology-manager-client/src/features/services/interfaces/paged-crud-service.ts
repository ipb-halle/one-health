import { IQueryCommand } from "../../filter";
import { CrudService } from "./crud-service";
import { IHttpResponseHandlerSettings } from "../utils/http-responses-handler";
import { IPagedData } from "../paged-data";
import { injectable } from "inversify";
import axios from "axios";
import { RefObject } from "react";
import { Toast } from "primereact/toast";
import { OnReadByIdResponsesHandler } from "../utils/http-responses-handler";
import { flatten } from "../../utils";
import { constructHttpParams } from "../../utils/flatten";
import { MessageService } from "../../messages";



@injectable()
export class PagedCrudService<TEntity> extends CrudService<TEntity> {
    
    getPage(queryCommand: IQueryCommand, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings) : Promise<any> {
        var filters = Object.keys(queryCommand.filters).map(x => queryCommand.filters[x]);

       
        // filters = filters.filter((x : any) => { return x.value});
        var query = { first: queryCommand.first, rows: queryCommand.rows, page: queryCommand.page, sortField: queryCommand.sortField, sortOrder: queryCommand.sortOrder, filters: filters };
        console.log(constructHttpParams(query));
        return this.handleRequest<IPagedData<TEntity>>(
            axios.get<any>(`${this.url}/getPage`, { params: constructHttpParams(query), paramsSerializer: { indexes: false } }),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    }
    
    /** 
    * To be overridden in child services to pass some data from the component to as the default filter conditions 
    * (e.g. you might want to append some custom filter conditions without overriding of the getPage method).
    * @param {IQueryCommand} queryCommand
    */
    extendQueryCommand(queryCommand: IQueryCommand) : void { }

}