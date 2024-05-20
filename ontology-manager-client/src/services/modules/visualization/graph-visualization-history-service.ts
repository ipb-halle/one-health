import { injectable } from "inversify";
import { CrudService } from "../../interfaces/crud-service";
import { ISavedGraphVisualization } from "../../../features/modules/visualization";
import { IHttpResponseHandlerSettings } from "../../../features/shared/http/http-responses-handler";
import { MessageService } from "../../../features/shared/messages";

@injectable()
export class IGraphVisualizationHistoryService extends CrudService<ISavedGraphVisualization>{
    url: string = "api/visualization-history"
    entityTitle: string = "Graph Visualization";

    getAllAsOptions(messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<Partial<ISavedGraphVisualization>[]>{
        throw new Error();
    }
};

@injectable()
export class MockGraphVisualizationHistoryService extends IGraphVisualizationHistoryService {
    private collection: ISavedGraphVisualization[] = [];

    get(id: string | number, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<ISavedGraphVisualization> {
        const result = this.collection.find(x => x.id === id);
        if (result)
            return Promise.resolve(result);
        throw new Error();
    }

    create(item: ISavedGraphVisualization, messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<ISavedGraphVisualization> {
        item.id = this.collection.length.toString();
        this.collection.push(item);
        return Promise.resolve(item);
    }

    delete(id: string | number, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<any> {
        this.collection = this.collection.filter(x => x.id !== id);
        return Promise.resolve(true);
    }

    getAllAsOptions(messageService: MessageService, httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined): Promise<Partial<ISavedGraphVisualization>[]> {
        return Promise.resolve(this.collection.map(x => <ISavedGraphVisualization>{id: x.id, name: x.name}));
    }
    


}


