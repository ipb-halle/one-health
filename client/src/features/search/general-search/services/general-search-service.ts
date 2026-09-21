import { injectable } from 'inversify';
import qs from 'qs';
import {
    IHttpResponseHandlerSettings,
    OnReadByIdResponsesHandler,
} from '../../../../core/api/http/http-responses-handler';
import { BaseDataService } from '../../../../core/api/http/interfaces/base-data-service';
import { MessageService } from '@/core/api/messages/interfaces/message-service';
import { httpFetch } from '@/core/api/http/http-client';

@injectable()
export class IGeneralSearchService extends BaseDataService {
    findEntities(
        value: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        throw Error();
    }
}

@injectable()
export class GeneralSearchService extends IGeneralSearchService {
    url: string = 'api/search';
    entityTitle: string = 'Search Result';

    findEntities(
        value: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<any> {
        const encodedValue = encodeURIComponent(value);
        const query = { query: encodedValue };
        const queryString = qs.stringify(query);
        const fullUrl = `${this.url}?${queryString}`;

        return this.handleRequest<any>(
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
        );
    }
}
