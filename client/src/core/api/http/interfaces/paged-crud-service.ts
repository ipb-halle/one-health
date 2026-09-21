import { IQueryCommand } from '../models/query-command';
import { CrudService } from './crud-service';
import { IHttpResponseHandlerSettings } from '../http-responses-handler';
import { IPagedData } from '../models/paged-data';
import { injectable } from 'inversify';
import { OnReadByIdResponsesHandler } from '../http-responses-handler';
import { constructHttpParams } from '../../../../shared';
import { MessageService } from '@/core/api/messages/interfaces/message-service';
import qs from 'qs';
import { httpFetch } from '@/core/api/http/http-client';

@injectable()
export class PagedCrudService<TEntity> extends CrudService<TEntity> {
    getPage(
        queryCommand: IQueryCommand,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        // var filters = Object.keys(queryCommand.filters).map(x => queryCommand.filters[x]);

        // filters = filters.filter((x : any) => { return x.value});
        const query = {
            first: queryCommand.first,
            rows: queryCommand.rows,
            page: queryCommand.page,
            sortField: queryCommand.sortField,
            sortOrder: queryCommand.sortOrder,
        };
        const qparams = constructHttpParams(query);
        const queryString = qs.stringify(qparams, { indices: false });
        const fullUrl = `${this.url}/getPage?${queryString}`;

        return this.handleRequest<IPagedData<TEntity>>(
            httpFetch(fullUrl, {
                method: 'GET',
                headers: {
                    Accept: 'application/json',
                },
            }),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        )
            .then((x) => x)
            .catch((x) => x);
    }

    /**
     * To be overridden in child services to pass some data from the component to as the default filter conditions
     * (e.g. you might want to append some custom filter conditions without overriding of the getPage method).
     * @param {IQueryCommand} queryCommand
     */
    extendQueryCommand(queryCommand: IQueryCommand): void {}
}
