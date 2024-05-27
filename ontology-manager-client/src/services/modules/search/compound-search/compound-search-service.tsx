import { injectable } from "inversify";
import { BaseDataService } from "../../../interfaces/base-data-service";
import { MessageService } from "../../../../features/shared/messages";
import { IHttpResponseHandlerSettings, OnReadByIdResponsesHandler } from "../../../../features/shared/http/http-responses-handler";
import axios from "axios";
import { constructHttpParams } from "../../../../utils";

@injectable()
export class ICompoundService extends BaseDataService{

    getBySMILES(value: string, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        throw Error();
    };

    getByInChI(value: string, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        throw Error();
    };

    getByInChIKey(value: string, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        throw Error();
    };

    getBySubstructure(smiles: string, take: number, page: number, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        throw Error();
    };

    getBySimilarity(smiles: string, threshold: number, limit: number, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        throw Error();
    };


};

@injectable()
export class CompoundService extends ICompoundService {
    url: string = "api/compounds"
    entityTitle: string = "Compound";


    getBySMILES(value: string, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<any> {
        const query = { query: value};
        const parsed = constructHttpParams(query);
        
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/by-smiles/${parsed.query}`),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    }

    getByInChI(value: string, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<any> {
        const query = { query: value};
        const parsed = constructHttpParams(query);
        
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/by-inchi/${query.query}}`),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    }

    getByInChIKey(value: string, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<any> {
        const query = { query: value};
        const parsed = constructHttpParams(query);
        
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/by-inchikey/${parsed.query}`),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        ).then(x => x).catch(x => x);
    }

    getBySubstructure(smiles: string, take: number, page: number, messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
         const query = {
            smiles: smiles,
            take: take,
            page: page
         }

        const parsed = constructHttpParams(query);

        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/by-substructure`, { params: parsed }),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        )

    };

    getBySimilarity(smiles: string, threshold: number, limit: number, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<any> {
        const query = {
            smiles: smiles,
            threshold: threshold,
            limit: limit
         }

        const parsed = constructHttpParams(query);

        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/by-similarity`, { params: parsed }),
            new OnReadByIdResponsesHandler(this.entityTitle, messageService, httpResponseHandlerSettings)
        )
    }
    
}


