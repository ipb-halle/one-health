import { injectable } from 'inversify';
import { httpRequest } from '../http-client';
import { BaseDataService } from './base-data-service';
import { IHttpResponseHandlerSettings } from '../http-responses-handler';
import { OnReadByIdResponsesHandler } from '../http-responses-handler';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

import { constructHttpParams } from '../../../../shared';

/**
 * Provides base implementations of the standard CRUD operations
 * @extends BaseDataService
 */
@injectable()
export class GraphService extends BaseDataService {
    getInitial(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        return this.handleRequest<any>(
            httpRequest<any>(`${this.url}/get-initial`),
            new OnReadByIdResponsesHandler(
                'graph',
                messageService,
                httpResponseHandlerSettings,
            ),
        )
            .then((x) => x)
            .catch((x) => x);
    }

    getNode(
        id: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        return this.handleRequest<any>(
            httpRequest<any>(`${this.url}/get-node/${id}`),
            new OnReadByIdResponsesHandler(
                'graph',
                messageService,
                httpResponseHandlerSettings,
            ),
        );
    }

    getEdge(
        id: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        return this.handleRequest<any>(
            httpRequest<any>(`${this.url}/get-edge/${id}`),
            new OnReadByIdResponsesHandler(
                'graph',
                messageService,
                httpResponseHandlerSettings,
            ),
        )
            .then((x) => x)
            .catch((x) => x);
    }

    getLinksBetween(
        sourceId: string,
        targetId: string,
        type: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        const query = { sourceId: sourceId, targetId: targetId, type: type };
        return this.handleRequest<any>(
            httpRequest<any>(
                `${this.url}/get-links-between?${constructHttpParams(query).toString()}`,
            ),
            new OnReadByIdResponsesHandler(
                'graph',
                messageService,
                httpResponseHandlerSettings,
            ),
        );
    }

    getNodeExpansion(
        id: string,
        nodes: string[],
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        return this.handleRequest<any>(
            httpRequest<any>(`${this.url}/get-node-expansion/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(nodes),
            }),
            new OnReadByIdResponsesHandler(
                'graph',
                messageService,
                httpResponseHandlerSettings,
            ),
        )
            .then((x) => x)
            .catch((x) => x);
    }
}
