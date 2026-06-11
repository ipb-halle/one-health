import { PagedCrudService } from '../../../core/api/http/interfaces/paged-crud-service';
import { IHttpResponseHandlerSettings } from '../../../core/api/http/http-responses-handler';
import { injectable } from 'inversify';
import { IProperty } from '../properties';

@injectable()
export class IPropertyService extends PagedCrudService<IProperty> {}

@injectable()
export class PropertyService extends IPropertyService {
    url: string = '/property/';
    entityTitle: string = 'Property';

    // get(id: number | string, httpResponseHandlerSettings? : IHttpResponseHandlerSettings): string {
    //     return "Property";
    // };
}
