export interface IGridSettings {
    editingEnabled: boolean;
    editFunc?: (rowData: any, rowIndex: number) => void;
    deletingEnabled: boolean;
    deleteFunc?: (rowData: any, rowIndex: number) => void;
}
