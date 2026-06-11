export interface IQueryCommand {
    first: number;
    rows: number;
    page: number | undefined;
    sortField?: string;
    sortOrder?: 0 | 1 | -1 | null;
    filters?: any;
}
