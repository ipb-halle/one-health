import { IHttpResponseHandlerSettings } from '../../../core/api/http/http-responses-handler';
import { injectable } from 'inversify';
import { PagedCrudService } from '../../../core/api/http/interfaces/paged-crud-service';
import { SelectableOption } from '../../../core/types/selectable-option';
import { IEntityType } from '../entity-types';
import { MessageService } from '@/core/api/messages/interfaces/message-service';
import { httpFetch } from '@/core/api/http/http-client';


@injectable()
export class IEntityTypeService extends PagedCrudService<IEntityType> {
    getAllEntityTypesAsOptions(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<SelectableOption[]> {
        return this.handleRequest<SelectableOption[]>(
            httpFetch(`${this.url}/as-options`, {
                method: 'GET',
                headers: {
                    Accept: 'application/json',
                },
            }),
        )
            .then((x) => x)
            .catch((x) => x);
    }
}

@injectable()
export class EntityTypeService extends IEntityTypeService {
    url: string = 'api/entity-type';
    entityTitle: string = 'Entity Type';
}
