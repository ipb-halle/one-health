import { injectable } from "inversify";
import { BaseDataService } from "../../../interfaces/base-data-service";
import { MessageService } from "../../../../features/shared/messages";
import { IHttpResponseHandlerSettings, OnReadByIdResponsesHandler } from "../../../../features/shared/http/http-responses-handler";
import axios from "axios";
import { constructHttpParams } from "../../../../utils";

@injectable()
export class IGeneralSearchService extends BaseDataService{

    findEntities(value: string, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        throw Error();
    };


};

@injectable()
export class GeneralSearchService extends IGeneralSearchService {
    url: string = "api/search"
    entityTitle: string = "Search Result";


   findEntities(value: string, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<any> {
    const query = { query: value};
    const parsed = constructHttpParams(query);
    
    return this.handleRequest<any>(
        axios.get<any>(`${this.url}/${parsed.query}`),
        new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
    );

   }
    
}


