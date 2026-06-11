import { injectable } from 'inversify';
import { IHttpResponseHandlerSettings } from '../../../../core/api/http/http-responses-handler';
import data from '../models/cooccurrences-visualization-history.json';
import { CrudService } from '../../../../core/api/http/interfaces/crud-service';
import { ISavedCoOcurrenceVisualization } from '../../visualization-history/models/saved-co-ocurrence-visualization';
import { MessageService } from '@/core/api/messages/interfaces/message-service';


@injectable()
export class ICoOcurrenceVisualizationHistoryService extends CrudService<ISavedCoOcurrenceVisualization> {
    url: string = 'api/visualization-history/co-ocurrence';
    entityTitle: string = 'Graph Visualization';

    getAllAsOptions(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<Partial<ISavedCoOcurrenceVisualization>[]> {
        throw new Error();
    }
}

@injectable()
export class MockCoOcurrenceVisualizationHistoryService extends ICoOcurrenceVisualizationHistoryService {
    private collection: ISavedCoOcurrenceVisualization[] = [];

    constructor() {
        super();
        this.collection = data;
    }

    get(
        id: string | number,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<ISavedCoOcurrenceVisualization> {
        const result = this.collection.find((x) => x.id === id);
        if (result) return Promise.resolve(result);
        throw new Error();
    }

    create(
        item: ISavedCoOcurrenceVisualization,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<ISavedCoOcurrenceVisualization> {
        item.id = this.collection.length.toString();
        this.collection.push(item);
        return Promise.resolve(item);
    }

    delete(
        id: string | number,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<any> {
        this.collection = this.collection.filter((x) => x.id !== id);
        return Promise.resolve(true);
    }

    getAllAsOptions(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<Partial<ISavedCoOcurrenceVisualization>[]> {
        return Promise.resolve(
            this.collection.map(
                (x) =>
                    <ISavedCoOcurrenceVisualization>{ id: x.id, name: x.name },
            ),
        );
    }
}
