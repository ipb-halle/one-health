import { dependencyFactory } from "../../injection/inversify.config";
import { ILinkTypeService } from "../../services";
import { SERVICE_TYPES } from "../../services";
import { RefObject } from "react";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import { Column } from "primereact/column";
import LazyLoadGrid from "../../utils/grid/grid.component";


const React = require('react');

const LinkTypePage: React.FC = () => {
    const linkTypeService = dependencyFactory.get<ILinkTypeService>(SERVICE_TYPES.ILinkTypeService);
    const toast : RefObject<Toast> = useRef<Toast>(null);


    const filters = {
        "name": { value: '', matchMode: 'contains', dataType: "STRING" },
        "description": { value: '', matchMode: 'contains', dataType: "STRING" },
    };

    const getColums = () => {
        return [

            (<Column field="name" header="Name" sortable filter filterField="name" filterPlaceholder="Search" />),
            (<Column field="description" sortable header="Description" filterField="description" filter filterPlaceholder="Search" />)
        ]
    }



    return (
        <LazyLoadGrid filters={filters} columns={getColums()} service={linkTypeService} toast={toast}></LazyLoadGrid>
    );
};

export default LinkTypePage;
