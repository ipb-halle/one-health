import { injectable } from "inversify";
import { PagedCrudService } from "./interfaces/paged-crud-service";
import { IKeyword } from "../ontology/keywords";

@injectable()
export class IKeywordService extends PagedCrudService<IKeyword>{};


@injectable()
export class KeywordService extends IKeywordService {
    url: string = "api/keyword"
    entityTitle: string = "Keyword";
}









