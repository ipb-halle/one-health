
import { injectable } from "inversify";
import { GraphService } from "../../interfaces/graph-service";

@injectable()
export class IEntityService extends GraphService{
    url: string = "api/entity"
    entityTitle: string = "Entity";
};

@injectable()
export class EntityService extends IEntityService {}