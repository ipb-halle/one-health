
import React, { useState, useEffect, ReactNode } from 'react';
import { DataTable, DataTableSelectAllChangeEvent,
    DataTablePageEvent, DataTableSortEvent, DataTableFilterEvent } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { DataTableFilterMeta } from 'primereact/datatable';
import { IQueryCommand } from '../../features/filters';
import { PagedCrudService } from '../../services/interfaces/paged-crud-service';
import { filter } from 'rxjs';
import { faL } from '@fortawesome/free-solid-svg-icons';

export interface ILazyLoadGrid {
    columns: any,
    filters: any,
    service: PagedCrudService<any>,
    toast: any,
}


export const LazyLoadGrid: React.FC<ILazyLoadGrid> = ({columns, filters, service, toast}) => {
    const [loading, setLoading] = useState<boolean>(false);
    const [totalRecords, setTotalRecords] = useState<number>(0);
    const [records, setRecords] = useState<any>([]);
    const [selectAll, setSelectAll] = useState<boolean>(false);
    // const [lazyState, setlazyState] = useState<IQueryCommand>({
    //     first: 0,
    //     rows: 10,
    //     page: 1,
    //     sortField: undefined,
    //     sortOrder: undefined,
    //     // filters: filters
    // });

    // useEffect(() => {
    //     loadLazyData(lazyState);
    // }, [lazyState]);

    const loadLazyData = async (lazyState: IQueryCommand) => {
        // setLoading(true);

        console.log("cojone");
        var result = await service.getPage(lazyState, toast);

        // setLoading(false)
        setTotalRecords(result.total);
        setRecords(result.items);
    };

    // const onPage = async (event: DataTablePageEvent) => {
    //     setlazyState({...lazyState, first: event.first, rows: event.rows, page: event.page});
    // };

    // const onSort = (event: DataTableSortEvent) => {
    //     setlazyState({...lazyState, sortField: event.sortField, sortOrder: event.sortOrder?? 0});
    // };

    const onFilter = (event: DataTableFilterEvent) => {
        event['first'] = 0;
        // setlazyState({...lazyState, filters: event.filters });
    };


    return (
        <div className="card">
            {/* <DataTable value={records} lazy filterDisplay="row" dataKey="id" paginator
                    first={lazyState.first} rows={10} totalRecords={totalRecords} onPage={onPage}
                    onSort={onSort} sortField={lazyState.sortField} sortOrder={lazyState.sortOrder}
                    onFilter={onFilter} filters={lazyState.filters} loading={loading} tableStyle={{ minWidth: '75rem' }}>
                {columns.map((x:any) => {return x})}
            </DataTable> */}
        </div>
    );
}
        

export default LazyLoadGrid