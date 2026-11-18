import { httpRequest } from '@/core/api/http/http-client';
import { injectable } from 'inversify';
import {
    IHttpResponseHandlerSettings,
    OnCreateResponseHandler,
} from '@/core/api/http/http-responses-handler';
import { BaseDataService } from '@/core/api/http/interfaces/base-data-service';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

@injectable()
export class IDocumentService extends BaseDataService {
    uploadDocument(
        file: File,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<any> {
        throw Error();
    }
}

@injectable()
export class DocumentService extends IDocumentService {
    url: string = 'api/documents/upload';
    entityTitle: string = 'Document';

    uploadDocument(
        file: File,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings | undefined,
    ): Promise<any> {
        const formData = new FormData();
        formData.append('file', file);

        return this.handleRequest<any>(
            httpRequest<any>(`${this.url}`, {
                method: 'POST',
                body: formData,
            }),
            new OnCreateResponseHandler(
                this.entityTitle,
                messageService,
                httpResponseHandlerSettings,
            ),
        );
    }
}