import { Column } from 'primereact/column';
import { DataTable } from 'primereact/datatable';

import React from 'react';
interface IDataPreviewProps {
    header: any;
    body: any;
}

const DataPreview: React.FC<IDataPreviewProps> = ({ header, body }) => {
    return (
        <>
            {body.length > 0 && (
                <div style={{ border: '1px solid #dee2e6' }}>
                    <DataTable value={body} size="small">
                        {header.map((x: any) => (
                            <Column
                                field={x}
                                header={x}
                                style={{ width: '50px' }}></Column>
                        ))}
                    </DataTable>
                </div>
            )}
        </>
    );
};

export default DataPreview;
