import { injectable } from 'inversify';
import { BaseDataService } from './base-data-service';
import {
    IHttpResponseHandlerSettings,
    OnCreateResponseHandler,
    OnReadByIdResponsesHandler,
} from '../http-responses-handler';

import { httpRequest } from '../http-client';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

/**
 * Provides base implementations of the standard CRUD operations
 * @extends BaseDataService
 */
@injectable()
export class CrudService<TEntity> extends BaseDataService {
    readonly entityTitle: string = '';

    get(
        id: number | string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<TEntity> {
        //TODO:how to fix the catch here
        return this.handleRequest<TEntity>(
            httpRequest<TEntity>(`${this.url}/${id}`),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        );
    }

    getAll(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<TEntity[]> {
        return this.handleRequest<TEntity[]>(
            httpRequest<TEntity[]>(`${this.url}/all`),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        );
    }

    create(
        item: TEntity,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<TEntity> {
        return this.handleRequest<TEntity>(
            httpRequest<TEntity>(this.url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(item),
            }),

            new OnCreateResponseHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        );
    }
    update(
        id: number | string,
        item: TEntity,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<TEntity> {
        throw new Error();
    }
    delete(
        id: number | string,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        throw new Error();
    }
    deleteAll(
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        throw new Error();
    }
}
