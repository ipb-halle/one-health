import { PagedCrudService } from '../../../core/api/http/interfaces/paged-crud-service';
import { IHttpResponseHandlerSettings } from '../../../core/api/http/http-responses-handler';
import { injectable } from 'inversify';
import { IDataSource } from '../data-sources';
import axios from 'axios';
import { MessageService } from '@/core/api/messages/interfaces/message-service';


@injectable()
export class IDataSourceService extends PagedCrudService<IDataSource> {
    writeFile(
        id: string,
        text: string,
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<any> {
        return this.handleRequest<any>(
            axios.post<any>(`${this.url}/write-file/${id}`, text, {
                headers: {
                    'Content-Length': text.length,
                    'Content-Type': 'text/plain',
                },
            }),
        )
            .then((x) => x)
            .catch((x) => x);
    }
}

@injectable()
export class DataSourceService extends IDataSourceService {
    url: string = 'api/data-source';
    entityTitle: string = 'Data Source';

    // get(id: number | string, httpResponseHandlerSettings? : IHttpResponseHandlerSettings): string {
    //     return "Data Source";
    // };
}
