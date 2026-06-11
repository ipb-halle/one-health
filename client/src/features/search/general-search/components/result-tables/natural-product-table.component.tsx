import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import {
    nameColumnTemplate,
    smilesColumnTemplate,
    structureDrawTemplate,
    weightColumnTemplate,
} from './column-templates';
import { useContext } from 'react';

import { observer } from 'mobx-react-lite';
import { Entity } from '../../../../../store/Entity';
import { RootStoreContext } from '../../../../../app/providers/store-provider';
import { Instance } from 'mobx-state-tree';

interface NaturalProductTableProps {
    results: Instance<typeof Entity>[];
}

const NaturalProductTable: React.FC<NaturalProductTableProps> = ({
    results,
}) => {
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
            <Column header="Structure" body={structureDrawTemplate}></Column>
            <Column
                field="name"
                header="Mol. Formula"
                body={nameColumnTemplate}
                sortable></Column>
            <Column header="Mol.Weight" body={weightColumnTemplate}></Column>
            <Column header="SMILES" body={smilesColumnTemplate}></Column>
        </DataTable>
    );
};
export default observer(NaturalProductTable);
