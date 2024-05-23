import { injectable } from "inversify";
import axios from "axios";
import { BaseDataService } from "./base-data-service";
import { IHttpResponseHandlerSettings } from "../../features/shared/http/http-responses-handler";
import { OnReadByIdResponsesHandler } from "../../features/shared/http/http-responses-handler";
import { MessageService } from "../../features/shared/messages";
import { constructHttpParams } from "../../utils";


/**
* Provides base implementations of the standard CRUD operations
* @extends BaseDataService
*/
@injectable()
export class GraphService extends BaseDataService {

    getInitial(messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : any {
        return this.handleRequest<any>(
            axios.get<any>(
                `${this.url}/get-initial`, {}
            ),
            new OnReadByIdResponsesHandler("graph", messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    }

    getNode(id: string, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : any {
        return this.handleRequest<any>(
            axios.get<any>(
                `${this.url}/get-node/${id}`, {}
            ),
            new OnReadByIdResponsesHandler("graph", messageService, httpResponseHandlerSettings)
        );
    }

    getEdge(id: string, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : any {
        return this.handleRequest<any>(
            axios.get<any>(
                `${this.url}/get-edge/${id}`, {}
            ),
            new OnReadByIdResponsesHandler("graph", messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    }

    getLinksBetween(sourceId: string, targetId: string, type:string, messageService:MessageService, httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : any {
        const query = {sourceId: sourceId, targetId: targetId, type: type};
        return this.handleRequest<any>(
            axios.get<any>(
                `${this.url}/get-links-between`, { params: constructHttpParams(query), paramsSerializer: { indexes: false } }
            ),
            new OnReadByIdResponsesHandler("graph", messageService, httpResponseHandlerSettings)
        );
    }


    getNodeExpansion(id: string, nodes: string[], messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : any {
        return this.handleRequest<any>(
            axios.post<any>(
                `${this.url}/get-node-expansion/${id}`, nodes 
            ),
            new OnReadByIdResponsesHandler("graph", messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    }

}