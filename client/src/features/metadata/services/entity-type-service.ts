import { IHttpResponseHandlerSettings } from '../../../core/api/http/http-responses-handler';
import { injectable } from 'inversify';
import { PagedCrudService } from '../../../core/api/http/interfaces/paged-crud-service';
import { SelectableOption } from '../../../core/types/selectable-option';
import { httpRequest } from '@/core/api/http/http-client';
import { IEntityType } from '../entity-types';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

@injectable()
export class IEntityTypeService extends PagedCrudService<IEntityType> {
    getAllEntityTypesAsOptions(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<SelectableOption[]> {
        return this.handleRequest<SelectableOption[]>(
            httpRequest<SelectableOption[]>(`${this.url}/as-options`),
        );
    }
}

@injectable()
export class EntityTypeService extends IEntityTypeService {
    url: string = 'api/entity-type';
    entityTitle: string = 'Entity Type';
}
