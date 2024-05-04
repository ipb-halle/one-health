import { IHttpResponseHandlerSettings } from "./utils/http-responses-handler";
import { injectable } from "inversify";
import { BaseDataService } from "./interfaces/base-data-service";
import axios from "axios";
import { OnReadByIdResponsesHandler } from "./utils/http-responses-handler";
import { ICoOcurrenceQuery } from "../data-visualization/co-ocurrence-search/co-ocurrence-query";
import { constructHttpParams } from "../utils/flatten";
import { MessageService } from "../messages";
const qs = require('qs');

@injectable()
export class IOntologyService extends BaseDataService{
    url: string = "api/ontology"
    entityTitle: string = "Ontology";


    getCoOcurrences(query: ICoOcurrenceQuery, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : any {
        var qparams = constructHttpParams(query);
        console.log(qparams);
        console.log("ahora no");
        console.log(qs.stringify(qparams));
        console.log("ahora si");
        
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

}

@injectable()
export class OntologyService extends IOntologyService {
  


   
}