import { injectable } from 'inversify';
import { BaseDataService } from '../../../../core/api/http/interfaces/base-data-service';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

import {
    IHttpResponseHandlerSettings,
    OnReadByIdResponsesHandler,
} from '../../../../core/api/http/http-responses-handler';
import { httpRequest } from '@/core/api/http/http-client';
import { constructHttpParams } from '../../../../shared';
import qs from 'qs';

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

        return this.handleRequest<any>(
            httpRequest<any>(
                `${this.url}/by-smiles?${qs.stringify(query)}`,
            ),
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

        return this.handleRequest<any>(
            httpRequest<any>(
                `${this.url}/by-inchi?${qs.stringify(query)}`,
            ),
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

        return this.handleRequest<any>(
            httpRequest<any>(
                `${this.url}/by-inchikey?${qs.stringify(query)}`,
            ),
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

        return this.handleRequest<any>(
            httpRequest<any>(
                `${this.url}/by-substructure?${parsed.toString()}`,
            ),
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

        return this.handleRequest<any>(
            httpRequest<any>(
                `${this.url}/by-similarity?${parsed.toString()}`,
            ),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        );
    }
}
