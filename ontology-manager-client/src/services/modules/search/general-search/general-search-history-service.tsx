import { injectable } from "inversify";
import { CrudService } from "../../../interfaces/crud-service";
import { ISavedGeneralSearch } from "../../../../features/modules/search/search-history/saved-general-search";
import { MessageService } from "../../../../features/shared/messages";
import { IHttpResponseHandlerSettings } from "../../../../features/shared/http/http-responses-handler";

@injectable()
export class IGeneralSearchHistoryService extends CrudService<ISavedGeneralSearch>{
    url: string = "api/visualization-history/co-ocurrence"
    entityTitle: string = "Graph Visualization";

    getAllAsOptions(messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<Partial<ISavedGeneralSearch>[]>{
        throw new Error();
    }
};

@injectable()
export class MockGeneralSearchHistoryService extends IGeneralSearchHistoryService {
    private collection: ISavedGeneralSearch[] = [];
    private counter : number = 0;
    
    constructor() {
        super();
    }

    get(id: string | number, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<ISavedGeneralSearch> {
        const result = this.collection.find(x => x.id === id);
        if (result)
            return Promise.resolve(result);
        throw new Error();
    }

    create(item: ISavedGeneralSearch, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<ISavedGeneralSearch> {
        item.id = this.counter.toString();
        item.datetime = Date();
        this.collection.push(item);
        this.counter++;
        return Promise.resolve(item);
    }

    delete(id: string | number, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<any> {
        this.collection = this.collection.filter(x => x.id !== id);
        return Promise.resolve(true);
    }

    getAllAsOptions(messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<Partial<ISavedGeneralSearch>[]> {
        const reversed = this.collection.map((x:any) => { return {id: x.id, query: x.query}});
        reversed.reverse();
        return Promise.resolve(reversed);
    }
    


}


