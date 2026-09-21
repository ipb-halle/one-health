import { injectable } from 'inversify';
import { BaseDataService } from '../../../../core/api/http/interfaces/base-data-service';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

import {
    IHttpResponseHandlerSettings,
    OnReadByIdResponsesHandler,
} from '../../../../core/api/http/http-responses-handler';
import { constructHttpParams } from '../../../../shared';
import qs from 'qs';
import { httpFetch } from '@/core/api/http/http-client';

@injectable()
export class ICompoundService extends BaseDataService {
    getBySMILES(
        value: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        throw Error();
    }

    getByInChI(
        value: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        throw Error();
    }

    getByInChIKey(
        value: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        throw Error();
    }

    getBySubstructure(
        smiles: string,
        take: number,
        page: number,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        throw Error();
    }

    getBySimilarity(
        smiles: string,
        threshold: number,
        limit: number,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        throw Error();
    }
}

@injectable()
export class CompoundService extends ICompoundService {
    url: string = 'api/compounds';
    entityTitle: string = 'Compound';

    getBySMILES(
        value: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<any> {
        const query = { value: value };
        const queryString = qs.stringify(query);
        const fullUrl = `${this.url}/by-smiles?${queryString}`;

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

    getByInChI(
        value: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<any> {
        const query = { value: value };
        const queryString = qs.stringify(query);
        const fullUrl = `${this.url}/by-inchi?${queryString}`;

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

    getByInChIKey(
        value: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<any> {
        const query = { value: value };
        const queryString = qs.stringify(query);
        const fullUrl = `${this.url}/by-inchikey?${queryString}`;

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

    getBySubstructure(
        smiles: string,
        take: number,
        page: number,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        const query = {
            smiles: smiles,
            take: take,
            page: page,
        };

        const parsed = constructHttpParams(query);
        const queryString = qs.stringify(parsed);
        const fullUrl = `${this.url}/by-substructure?${queryString}`;

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

    getBySimilarity(
        smiles: string,
        threshold: number,
        limit: number,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<any> {
        const query = {
            smiles: smiles,
            threshold: threshold,
            limit: limit,
        };

        const parsed = constructHttpParams(query);
        const queryString = qs.stringify(parsed);
        const fullUrl = `${this.url}/by-similarity?${queryString}`;

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
