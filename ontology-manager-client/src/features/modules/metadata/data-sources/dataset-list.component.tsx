import React from 'react';
import { Panel } from 'primereact/panel';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { IDataSource } from './data-source';
import { Button } from 'primereact/button';
import "./dataset-list.component.scss";
import { CollectionPlaceholderComponent } from '../../../../components';
const DatasetList: React.FC = () => {

    const headerIcons = () => {
        return <Button icon='pi pi-plus' severity='success' rounded outlined style={{width:"25px", height:"25px"}}  ></Button>
    };
    return (
        <Panel className="dataset-list" header="Data Sources" icons={headerIcons} style={{height:"100%"}}>
            
            <CollectionPlaceholderComponent icon='pi pi-database' message=''/>
            {/* <DataTable showGridlines tableStyle={{ lineHeight: '10px' }}>
            <Column field="name" header="Name"></Column>
            <Column field="dateAdded" header="Date Added"></Column>
            <Column field="inUse" header="In Use" style={{ width: '18%' }} dataType="boolean"></Column>
        </DataTable> */}
        </Panel>
    );
};

export default DatasetList;
