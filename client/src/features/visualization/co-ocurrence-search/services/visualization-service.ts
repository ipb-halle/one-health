import { IHttpResponseHandlerSettings } from '../../../../core/api/http/http-responses-handler';
import { injectable } from 'inversify';
import { BaseDataService } from '../../../../core/api/http/interfaces/base-data-service';
import { OnReadByIdResponsesHandler } from '../../../../core/api/http/http-responses-handler';
import { constructHttpParams } from '../../../../shared/utils/flatten';
import { ICoOcurrenceQuery } from '../models/co-ocurrence-query';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

import qs from 'qs';
import { httpFetch } from '@/core/api/http/http-client';

@injectable()
export class IOntologyService extends BaseDataService {
    url: string = 'api/ontology';
    entityTitle: string = 'Ontology';

    getCoOcurrences(
        query: ICoOcurrenceQuery,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        const qparams = constructHttpParams(query);
        const queryString = qs.stringify(qparams);
        const fullUrl = `${this.url}/find-co-ocurrences?${queryString}`;

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

    getCoOccurrencesDetails(
        query: ICoOcurrenceQuery,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        const qparams = constructHttpParams(query);
        const queryString = qs.stringify(qparams);
        const fullUrl = `${this.url}/find-co-occurrences-details?${queryString}`;

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

@injectable()
export class OntologyService extends IOntologyService {}
