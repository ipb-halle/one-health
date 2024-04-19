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
import { ICoOcurrenceQuery } from "../data-visualization/co-ocurrence-search/co-ocurrence-query";
import { constructHttpParams } from "../utils/flatten";
const qs = require('qs');

@injectable()
export class IOntologyService extends BaseDataService{
    url: string = "api/ontology"
    entityTitle: string = "Ontology";


    getCoOcurrences(query: ICoOcurrenceQuery, toast : RefObject<Toast>,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings) : any {
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
            new OnReadByIdResponsesHandler(this.entityTitle, toast, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    }

}

@injectable()
export class OntologyService extends IOntologyService {
  


   
}