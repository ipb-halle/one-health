
import { injectable } from "inversify";



@injectable()
export class INeighborhoodExplorerStore {

    getIds(): any[] {
        throw Error();
    }

    setIds(value: any[]){
        throw Error;
    }

};

@injectable()
export class NeighborhoodExplorerStore extends INeighborhoodExplorerStore {

    ids : any[] = [];

    getIds(): any[] {
        return this.ids;
    }


    setIds(value: any[]): void {
        this.ids = [...value];
    }

    

}