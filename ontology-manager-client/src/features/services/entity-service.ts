import { IHttpResponseHandlerSettings } from "./utils/http-responses-handler";
import { injectable } from "inversify";
import axios from "axios";
import { OnReadByIdResponsesHandler } from "./utils/http-responses-handler";
import { SelectableOption } from "../utils/selectable-option";
import { GraphService } from "./interfaces/graph-service";
import { MessageService } from "../messages";

@injectable()
export class IEntityService extends GraphService{
    url: string = "api/entity"
    entityTitle: string = "Entity";
};

@injectable()
export class EntityService extends IEntityService {}