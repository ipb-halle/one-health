import { PagedCrudService } from "./paged-crud-service";
import { IHttpResponseHandlerSettings } from "./http-responses-handler";
import { injectable } from "inversify";
import { IDataSource } from "../ontology/data-sources";
import { RefObject } from "react";
import { Toast } from "primereact/toast";
import axios from "axios";
import { config } from "localforage";

@injectable()
export class IDataSourceService extends PagedCrudService<IDataSource>{

    writeFile(id: string, text: string, toast : RefObject<Toast>,  httpResponseHandlerSettings? : IHttpResponseHandlerSettings): Promise<any> {
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