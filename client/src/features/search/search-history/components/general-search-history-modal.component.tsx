import { useContext } from 'react';
import { observer } from 'mobx-react-lite';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { RootStoreContext } from '../../../../app/providers/store-provider';
import { Column } from 'primereact/column';
import { truncateString } from '../../../../shared';
import { HistoryItem } from '../../general-search/models/history-item';
import { DataView } from 'primereact/dataview';
import { DataTable } from 'primereact/datatable';
import { Instance } from 'mobx-state-tree';
import { SavedTextSearch } from '../models/SavedTextSearch';

type HistoryModalProps = {
    visible: boolean;
    onHide: () => void;
};

const HistoryModal = ({ visible, onHide }: HistoryModalProps) => {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    const historySearchStore = useContext(RootStoreContext).historySearchStore;

    const handleReuse = (entry: string) => {
        generalSearchStore.setQuery(entry);
        onHide?.();
    };

    const footer = (
        <div>
            <Button
                label="Clear History"
                icon="pi pi-trash"
                severity="danger"
                onClick={() => historySearchStore.clearHistory()}
            />
        </div>
    );

    const historyColumnTemplate = (item: Instance<typeof SavedTextSearch>) => {
        return (
            <div
                style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                }}>
                <div style={{}}>
                    <span style={{ color: 'black', marginLeft: '2px' }}>
                        {truncateString(item.query, 150)}
                    </span>
                </div>
                <div>
                    <Button
                        style={{ margin: '10px' }}
                        aria-label="Reuse"
                        icon="pi pi-refresh"
                        size="small"
                        onClick={() => handleReuse(item.query)}
                        tooltip="Reuse"
                        tooltipOptions={{
                            position: 'bottom',
                            showDelay: 1000,
                        }}
                    />
                    <Button
                        style={{ margin: '10px' }}
                        aria-label="Delete"
                        severity="danger"
                        icon="pi pi-times"
                        size="small"
                        tooltip="Delete"
                        tooltipOptions={{
                            position: 'bottom',
                            showDelay: 1000,
                        }}
                        onClick={() =>
                            historySearchStore.deleteHistoryItem(item)
                        }
                    />
                </div>
            </div>
        );
    };

    return (
        /*         <Dialog
                    header="Search History"
                    visible={visible}
                    onHide={onHide}
                    style={{ width: "40vw", minWidth: "320px" }}
                    modal
                >
                    <div>
        
                        <DataTable
                            sortField="name"
                            sortOrder={1}
                            value={historySearchStore.getHistoryAsJSON()}
        
                        >
                            <Column field="query" header="Queries" body={historyColumnTemplate} sortable></Column>
                        </DataTable>
        
        
        
                    </div>
                </Dialog> */
        <Dialog
            header="Search History"
            visible={visible}
            onHide={onHide}
            footer={footer}
            style={{ width: '40vw', minWidth: '320px' }}
            modal>
            <div>
                <DataTable
                    sortField="name"
                    sortOrder={1}
                    value={historySearchStore.getHistoryAsJSON()}>
                    <Column
                        field="query"
                        header="Queries"
                        body={historyColumnTemplate}
                        sortable></Column>
                </DataTable>
            </div>
        </Dialog>
    );
};
export default observer(HistoryModal);
