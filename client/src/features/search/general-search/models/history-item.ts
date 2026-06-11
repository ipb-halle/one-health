export interface HistoryItem {
    id: string | number;
    datetime: string | Date;
    query: string;
    results: any;
}
