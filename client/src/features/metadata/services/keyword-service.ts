import { injectable } from 'inversify';
import { PagedCrudService } from '../../../core/api/http/interfaces/paged-crud-service';
import { IKeyword } from '../keywords';
@injectable()
export class IKeywordService extends PagedCrudService<IKeyword> {}

@injectable()
export class KeywordService extends IKeywordService {
    url: string = 'api/keyword';
    entityTitle: string = 'Keyword';
}
