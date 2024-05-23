import { IQueryGraph } from "./graph-query";

class ListService {
    private static instance: ListService;
    private items: IQueryGraph[] = [];
  
    private constructor() {
      // Private constructor to prevent direct instantiation
    }
  
    static getInstance(): ListService {
      if (!ListService.instance) {
        ListService.instance = new ListService();
        Object.freeze(ListService.instance); // Optional: Freeze the instance to prevent modifications
      }
      return ListService.instance;
    }
  
    addItem(item: IQueryGraph) {
      this.items.push(item);
    }
  
    getItems(): IQueryGraph[] {
      return this.items;
    }
  }
  
export default ListService