export interface IGridColumn {
    key: string;
    header: string;
    sortable?: boolean;
    displayHandler?: (cellValue: any, rowValue: any) => string;
}
