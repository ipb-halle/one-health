import { id, injectable } from "inversify";
import { IQueryGraph } from "./graph-query";

@injectable()
export class IQueryHistoryGraphService {
    getAsOptions() : IQueryGraph[] { throw new Error()};

    get(id: string){};

    save(query: IQueryGraph) {};
};


@injectable()
export class QueryHistoryGraphService extends IQueryHistoryGraphService {
    url: string = "api/history/graph"
    entityTitle: string = "Query";

    constructor(){
        super();
        console.log("a new query history service");
    }

    private queries: IQueryGraph[] = [
        {
            id:"1",
            name:"Hello",
            query: "hdlf"
        }

    ];


    getAsOptions() : IQueryGraph[] {
        return this.queries;
    }

    get(id: string) {
        return this.queries.find((x:IQueryGraph) => x.id === id);
    }

    save(query: IQueryGraph) {
        this.queries.push(query);
    }

}

const pinga = new QueryHistoryGraphService();
Object.freeze(pinga);

export {pinga};







