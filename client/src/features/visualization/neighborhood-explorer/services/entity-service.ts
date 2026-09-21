import { injectable } from 'inversify';
import { GraphService } from '@/core/api/http/interfaces/graph-service';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

import {
    IHttpResponseHandlerSettings,
    OnReadByIdResponsesHandler,
} from '../../../../core/api/http/http-responses-handler';

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
            fetch(`${this.url}/get-graph-references`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Accept: 'application/json',
                },
                body: JSON.stringify(nodes),
            }),
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
