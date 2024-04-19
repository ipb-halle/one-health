import { dependencyFactory } from "../../injection/inversify.config";
import { IEntityTypeService, SERVICE_TYPES } from "../../services";
import LazyLoadGrid from "../../utils/grid/grid.component";
import { Column } from "primereact/column";
import { RefObject } from "react";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import { Panel } from "primereact/panel";

const React = require('react');

const EntityTypePage: React.FC = () => {
    const entityTypeService = dependencyFactory.get<IEntityTypeService>(SERVICE_TYPES.IEntityTypeService);
    const toast : RefObject<Toast> = useRef<Toast>(null);


    const filters = {
        "name": { value: '', matchMode: 'contains', dataType: "STRING" },
        "description": { value: '', matchMode: 'contains', dataType: "STRING" },
        "parent.id": { value: '', matchMode: 'equals', dataType: "STRING" },
    };

    const getColums = () => {
        return [

            (<Column field="name" header="Name" sortable filter filterField="name" filterPlaceholder="Search" />),
            (<Column field="description" sortable header="Description" filterField="description" filter filterPlaceholder="Search" />),
            (<Column field="parent.id" sortable filter header="Parent" filterField="parent.id" filterPlaceholder="Search" />)
        ]
    }



    return (
        <LazyLoadGrid filters={filters} columns={getColums()} service={entityTypeService} toast={toast}></LazyLoadGrid>
    );
    
};

export default EntityTypePage;
