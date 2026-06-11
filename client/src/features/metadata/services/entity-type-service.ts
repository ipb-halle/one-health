import { IHttpResponseHandlerSettings } from '../../../core/api/http/http-responses-handler';
import { injectable } from 'inversify';
import { PagedCrudService } from '../../../core/api/http/interfaces/paged-crud-service';
import { SelectableOption } from '../../../core/types/selectable-option';
import axios from 'axios';
import { IEntityType } from '../entity-types';
import { MessageService } from '@/core/api/messages/interfaces/message-service';


@injectable()
export class IEntityTypeService extends PagedCrudService<IEntityType> {
    getAllEntityTypesAsOptions(
        messageService: MessageService,
        httpResponseHandlerSettings?: IHttpResponseHandlerSettings,
    ): Promise<SelectableOption[]> {
        return this.handleRequest<SelectableOption[]>(
            axios.get<SelectableOption[]>(`${this.url}/as-options`),
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
