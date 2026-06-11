import { injectable } from 'inversify';
import { GraphService } from '@/core/api/http/interfaces/graph-service';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

import {
    IHttpResponseHandlerSettings,
    OnReadByIdResponsesHandler,
} from '../../../../core/api/http/http-responses-handler';
import axios from 'axios';

@injectable()
export class IEntityService extends GraphService {
    url: string = 'api/entity';
    entityTitle: string = 'Entity';

    getGraphReferences(
        nodes: string[],
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        return this.handleRequest<any>(
            axios.post<any>(`${this.url}/get-graph-references`, nodes),
            new OnReadByIdResponsesHandler(
                'graph',
                messageService,
                httpResponseHandlerSettings,
            ),
        );
    }
}

@injectable()
export class EntityService extends IEntityService {}
