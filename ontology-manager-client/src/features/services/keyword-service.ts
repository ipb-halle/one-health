import { injectable } from "inversify";
import { PagedCrudService } from "./paged-crud-service";
import { IKeyword } from "../ontology/keywords";

// TODO: Wrap toast inside an interface IMessager or something like that

@injectable()
export class IKeywordService extends PagedCrudService<IKeyword>{};


@injectable()
export class KeywordService extends IKeywordService {
    url: string = "api/keyword"
    entityTitle: string = "Keyword";
}









