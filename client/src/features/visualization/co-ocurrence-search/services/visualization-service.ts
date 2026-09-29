import { IHttpResponseHandlerSettings } from '../../../../core/api/http/http-responses-handler';
import { injectable } from 'inversify';
import { BaseDataService } from '../../../../core/api/http/interfaces/base-data-service';
import { httpRequest } from '@/core/api/http/http-client';
import { OnReadByIdResponsesHandler } from '../../../../core/api/http/http-responses-handler';
import { constructHttpParams } from '../../../../shared/utils/flatten';
import { ICoOcurrenceQuery } from '../models/co-ocurrence-query';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

import qs from 'qs';

@injectable()
export class IOntologyService extends BaseDataService {
    url: string = 'api/ontology';
    entityTitle: string = 'Ontology';

    getCoOcurrences(
        query: ICoOcurrenceQuery,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        var qparams = constructHttpParams(query);

        return this.handleRequest<any>(
            httpRequest<any>(
                `${this.url}/find-co-ocurrences${qs.stringify(qparams)}`,
            ),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        )
            .then((x) => x)
            .catch((x) => x);
    }

    getCoOccurrencesDetails(
        query: ICoOcurrenceQuery,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        var qparams = constructHttpParams(query);

        return this.handleRequest<any>(
            httpRequest<any>(
                `${this.url}/find-co-occurrences-details?${qs.stringify(qparams)}`,
            ),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        );
    }
}

@injectable()
export class OntologyService extends IOntologyService { }
