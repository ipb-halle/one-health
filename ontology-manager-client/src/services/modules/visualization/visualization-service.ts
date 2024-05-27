import { IHttpResponseHandlerSettings } from "../../../features/shared/http/http-responses-handler";
import { injectable } from "inversify";
import { BaseDataService } from "../../interfaces/base-data-service";
import axios from "axios";
import { OnReadByIdResponsesHandler } from "../../../features/shared/http/http-responses-handler";
import { constructHttpParams } from "../../../utils/flatten";
import { MessageService } from "../../../features/shared/messages";
import { ICoOcurrenceQuery } from "../../../features/modules/visualization/co-ocurrence-search/co-ocurrence-query";
const qs = require('qs');

@injectable()
export class IOntologyService extends BaseDataService{
    url: string = "api/ontology"
    entityTitle: string = "Ontology";


    getCoOcurrences(query: ICoOcurrenceQuery, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : any {
        var qparams = constructHttpParams(query);
        
        return this.handleRequest<any>(
            axios.get<any>(
                `${this.url}/find-co-ocurrences`, 
                { 
                    params: qparams, 
                    paramsSerializer: { 
                        serialize: (params) => 
                        {
                            return qs.stringify(params)
                        }
                    } 
                }
            ),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    }

    getCoOccurrencesDetails (query: ICoOcurrenceQuery, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : any {
        var qparams = constructHttpParams(query);

        return this.handleRequest<any>(
            axios.get<any>(
                `${this.url}/find-co-occurrences-details`, 
                { 
                    params: qparams, 
                    paramsSerializer: { 
                        serialize: (params) => 
                        {
                            return qs.stringify(params)
                        }
                    } 
                }
            ),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        )
    
    }


}

@injectable()
export class OntologyService extends IOntologyService {
  


   
}