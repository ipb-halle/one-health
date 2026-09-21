import { injectable } from 'inversify';
import { BaseDataService } from './base-data-service';
import { IHttpResponseHandlerSettings } from '../http-responses-handler';
import { OnReadByIdResponsesHandler } from '../http-responses-handler';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

import { constructHttpParams } from '../../../../shared';
import qs from 'qs';
import { httpFetch } from '@/core/api/http/http-client';

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
            httpFetch(`${this.url}/get-initial`, {
                method: 'GET',
                headers: {
                    Accept: 'application/json',
                },
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

    getNode(
        id: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        return this.handleRequest<any>(
            httpFetch(`${this.url}/get-node/${id}`, {
                method: 'GET',
                headers: {
                    Accept: 'application/json',
                },
            }),
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
            httpFetch(`${this.url}/get-edge/${id}`, {
                method: 'GET',
                headers: {
                    Accept: 'application/json',
                },
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

    getLinksBetween(
        sourceId: string,
        targetId: string,
        type: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        const query = { sourceId: sourceId, targetId: targetId, type: type };
        const qparams = constructHttpParams(query);
        const queryString = qs.stringify(qparams, { indices: false });
        const fullUrl = `${this.url}/get-links-between?${queryString}`;

        return this.handleRequest<any>(
            httpFetch(fullUrl, {
                method: 'GET',
                headers: {
                    Accept: 'application/json',
                },
            }),
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
            httpFetch(`${this.url}/get-node-expansion/${id}`, {
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
        )
            .then((x) => x)
            .catch((x) => x);
    }
}
