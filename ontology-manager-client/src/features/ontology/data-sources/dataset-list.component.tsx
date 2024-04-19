import React from 'react';
import { Panel } from 'primereact/panel';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { IDataSource } from './data-source';
import { Button } from 'primereact/button';
const DatasetList: React.FC = () => {

    const headerIcons = () => {
        return <Button icon='pi pi-plus' severity='success' rounded outlined style={{width:"25px", height:"25px"}}  ></Button>
    };
    return (
        <Panel header="Data Sources" icons={headerIcons} style={{height:"100%"}}>
            <div
                style={{ height: '545px' }}
                className="d-flex justify-content-center"
            >
                <div className="h-100  d-flex align-items-center">
                    <div className="row" style={{ width: '150px' }}>
                        <i
                            className="pi pi-database"
                            style={{
                                fontSize: '120px',
                                color: '#d9dde1',
                                height: '120px',
                            }}
                        ></i>
                        <span> No sources yet</span>
                    </div>
                </div>
            </div>
            {/* <DataTable showGridlines tableStyle={{ lineHeight: '10px' }}>
            <Column field="name" header="Name"></Column>
            <Column field="dateAdded" header="Date Added"></Column>
            <Column field="inUse" header="In Use" style={{ width: '18%' }} dataType="boolean"></Column>
        </DataTable> */}
        </Panel>
    );
};

export default DatasetList;
