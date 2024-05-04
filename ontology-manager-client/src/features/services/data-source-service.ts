import { PagedCrudService } from "./interfaces/paged-crud-service";
import { IHttpResponseHandlerSettings } from "./utils/http-responses-handler";
import { injectable } from "inversify";
import { IDataSource } from "../ontology/data-sources";
import axios from "axios";
import { MessageService } from "../messages";

@injectable()
export class IDataSourceService extends PagedCrudService<IDataSource>{

    writeFile(id: string, text: string,  messageService: MessageService,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
        return this.handleRequest<any>(
            axios.post<any>(`${this.url}/write-file/${id}`, text, {headers: {
                'Content-Length': text.length,
                'Content-Type': 'text/plain'
            },})
        ).then(x => x).catch(x => x);
    };

};


@injectable()
export class DataSourceService extends IDataSourceService {
    url: string = "api/data-source"
    entityTitle: string = "Data Source";

    // get(id: number | string, httpResponseHandlerSettings? : IHttpResponseHandlerSettings): string {
    //     return "Data Source";
    // };
}