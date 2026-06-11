import { RefObject } from 'react';
import { Toast } from 'primereact/toast';
import { useRef } from 'react';
import { Column } from 'primereact/column';
import { dependencyFactory } from '../../../app/di';
import { SERVICES } from '@/app/di/service-types';
import { ILinkTypeService } from '../services/link-type-service';
import LazyLoadGrid from '../../../shared/components/grid/grid.component';
import React from 'react';
const LinkTypePage: React.FC = () => {
    const linkTypeService = dependencyFactory.get<ILinkTypeService>(
        SERVICES.ILinkTypeService,
    );
    const toast: RefObject<Toast> = useRef<Toast>(null);

    const filters = {
        name: { value: '', matchMode: 'contains', dataType: 'STRING' },
        description: { value: '', matchMode: 'contains', dataType: 'STRING' },
    };

    const getColums = () => {
        return [
            <Column
                field="name"
                header="Name"
                sortable
                filter
                filterField="name"
                filterPlaceholder="Search"
            />,
            <Column
                field="description"
                sortable
                header="Description"
                filterField="description"
                filter
                filterPlaceholder="Search"
            />,
        ];
    };

    return (
        <LazyLoadGrid
            filters={filters}
            columns={getColums()}
            service={linkTypeService}
            toast={toast}></LazyLoadGrid>
    );
};

export default LinkTypePage;
