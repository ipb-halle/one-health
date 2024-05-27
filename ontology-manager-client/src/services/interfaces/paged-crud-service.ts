import { IQueryCommand } from "../models/query-command";
import { CrudService } from "./crud-service";
import { IHttpResponseHandlerSettings } from "../../features/shared/http/http-responses-handler";
import { IPagedData } from "../models/paged-data";
import { injectable } from "inversify";
import axios from "axios";
import { OnReadByIdResponsesHandler } from "../../features/shared/http/http-responses-handler";
import { constructHttpParams } from "../../utils";
import { MessageService } from "../../features/shared/messages";



@injectable()
export class PagedCrudService<TEntity> extends CrudService<TEntity> {
    
    getPage(queryCommand: IQueryCommand, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings) : Promise<any> {
        // var filters = Object.keys(queryCommand.filters).map(x => queryCommand.filters[x]);

       
        // filters = filters.filter((x : any) => { return x.value});
        var query = { first: queryCommand.first, rows: queryCommand.rows, page: queryCommand.page, sortField: queryCommand.sortField, sortOrder: queryCommand.sortOrder };
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