import React, { useState, useEffect, ReactNode } from 'react';
import {
    DataTable,
    DataTableSelectAllChangeEvent,
    DataTablePageEvent,
    DataTableSortEvent,
    DataTableFilterEvent,
} from 'primereact/datatable';
import { Column } from 'primereact/column';
import { DataTableFilterMeta } from 'primereact/datatable';
import { IQueryCommand } from '../../../features/filters';
import { PagedCrudService } from '../../../core/api/http/interfaces/paged-crud-service';
import { filter } from 'rxjs';
import { faL } from '@fortawesome/free-solid-svg-icons';

export interface ILazyLoadGrid {
    columns: any;
    filters: any;
    service: PagedCrudService<any>;
    toast: any;
}

export const LazyLoadGrid: React.FC<ILazyLoadGrid> = ({
    columns,
    filters,
    service,
    toast,
}) => {
    const [loading, setLoading] = useState<boolean>(false);
    const [totalRecords, setTotalRecords] = useState<number>(0);
    const [records, setRecords] = useState<any>([]);
    const [selectAll, setSelectAll] = useState<boolean>(false);


    const loadLazyData = async (lazyState: IQueryCommand) => {


        var result = await service.getPage(lazyState, toast);

        setTotalRecords(result.total);
        setRecords(result.items);
    };

    const onFilter = (event: DataTableFilterEvent) => {
        event['first'] = 0;
    };

    return (
        <div className="card">
            { }
        </div>
    );
};

export default LazyLoadGrid;
