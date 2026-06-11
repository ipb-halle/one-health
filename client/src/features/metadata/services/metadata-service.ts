import { IHttpResponseHandlerSettings } from '../../../core/api/http/http-responses-handler';
import { injectable } from 'inversify';
import axios from 'axios';
import { OnReadByIdResponsesHandler } from '../../../core/api/http/http-responses-handler';
import { SelectableOption } from '../../../core/types/selectable-option';
import { GraphService } from '../../../core/api/http/interfaces/graph-service';
import { MessageService } from '@/core/api/messages/interfaces/message-service';


@injectable()
export class IMetadataService extends GraphService {
    url: string = 'api/metadata';
    entityTitle: string = 'Metadata';

    getAll(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/all`),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        )
            .then((x) => x)
            .catch((x) => x);
    }

    getEntityType(
        id: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/entity-type/${id}`),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        )
            .then((x) => x)
            .catch((x) => x);
    }

    getLinkType(
        id: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/link-type/${id}`),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        )
            .then((x) => x)
            .catch((x) => x);
    }

    getSummary(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        return this.handleRequest<any>(
            axios.get<any>(`${this.url}/summary`),
            new OnReadByIdResponsesHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        )
            .then((x) => x)
            .catch((x) => x);
    }

    getAllAsOptions(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<SelectableOption[]> {
        return this.handleRequest<SelectableOption[]>(
            axios.get<SelectableOption[]>(`${this.url}/as-options`),
        )
            .then((x) => x)
            .catch((x) => x);
    }

    getInitial(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        return this.getAll(messageService, httpResponseHandlerSettings);
    }

    getNode(
        id: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        return this.getEntityType(
            id,
            messageService,
            httpResponseHandlerSettings,
        );
    }

    getEdge(
        id: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): any {
        return this.getLinkType(
            id,
            messageService,
            httpResponseHandlerSettings,
        );
    }
}

@injectable()
export class MetadataService extends IMetadataService {}
