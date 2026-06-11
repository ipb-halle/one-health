import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import {
    familyColumnTemplate,
    kingdomColumnTemplate,
    nameColumnTemplate,
    phylumColumnTemplate,
} from './column-templates';
import { observer } from 'mobx-react-lite';
import { Instance } from 'mobx-state-tree';
import { Entity } from '../../../../../store/Entity';
import { RootStoreContext } from '../../../../../app/providers/store-provider';
import { useContext } from 'react';

interface PlantTableProps {
    results: Instance<typeof Entity>[];
}

const PlantTable: React.FC<PlantTableProps> = ({ results }) => {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    return (
        <DataTable
            metaKeySelection={false}
            selectionMode="multiple"
            selection={generalSearchStore.getSelectionAsJSON()}
            sortField="name"
            sortOrder={1}
            emptyMessage="No entries found... Try again!"
            onSelectionChange={(e) =>
                generalSearchStore.setSelectedEntities(e.value)
            }
            value={results}
            tableStyle={{ minWidth: '50rem' }}>
            <Column
                field="name"
                header="Scientific Name"
                body={nameColumnTemplate}
                sortable></Column>
            <Column header="Kingdom" body={kingdomColumnTemplate}></Column>
            <Column header="Phylum" body={phylumColumnTemplate}></Column>
            <Column header="Family" body={familyColumnTemplate}></Column>
        </DataTable>
    );
};
export default observer(PlantTable);
