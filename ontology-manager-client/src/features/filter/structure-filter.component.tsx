
import { RefObject, useEffect, useState, useRef } from "react";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import StructureEditor from "./structure-editor.component";
import { OverlayPanel } from 'primereact/overlaypanel';
import { Dropdown } from 'primereact/dropdown';
import { StructureFilterMatchMode } from "./enums/structure-filter-match-mode";
import { FilterComponentProps } from "./interfaces/filter-component.props";
import { FilterInfo } from "./interfaces/filter-info";

const React = require('react');




const ChemicalStructureFilter: React.FC<FilterComponentProps> = ({filterInfo, onChange}) => {
    const options = Object.entries(StructureFilterMatchMode).map(([key, value]) => {return {label: value, value: key}});

    const op = useRef<OverlayPanel>(null);

    const dataTypeSelectedTemplate = (option: any, props: any) => {
        return  <i style={{color: "#6C757D"}} className="pi pi-filter"></i>
    }

    useEffect(() => {
        onChange({...filterInfo, matchMode: Object.keys(StructureFilterMatchMode)[1]});
    }, [])


    return (<div>
        
        {filterInfo.property}:
        <div className="p-inputgroup flex-1">
            <span className="p-inputgroup-addon">
                <i className="pi pi-share-alt"></i>
            </span>
            <InputText/>
            <Button icon="pi pi-pencil" className="p-inputgroup-button"  onClick={(e) => op.current?.toggle(e)} />
            <Dropdown 
                className="p-filter-match-mode-dropdown"
                style={{maxWidth:"80px"}}
                options={options} 
                value={filterInfo.matchMode} 
                valueTemplate={dataTypeSelectedTemplate}
                onChange={(e) => onChange({...filterInfo, matchMode: e.value})}
            />
        </div>

        <OverlayPanel ref={op} showCloseIcon closeOnEscape dismissable={true}>
            <div style={{width: 500, height: 500}}>
                <StructureEditor></StructureEditor>
            </div>
        </OverlayPanel>
        
        </div>);

};

export default ChemicalStructureFilter;
