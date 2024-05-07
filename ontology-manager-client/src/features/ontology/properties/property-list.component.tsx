import React, { useContext, useRef, useState } from 'react';
import { IProperty } from './property';
import './property-list.component.scss';
import { Panel } from 'primereact/panel';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { DataTypes } from '../data-types/data-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faKey, faL } from '@fortawesome/free-solid-svg-icons';
import { InputText } from 'primereact/inputtext';
import { ColumnEvent } from 'primereact/column';
import { Checkbox } from 'primereact/checkbox';
import { Toast } from 'primereact/toast';
import { Dropdown } from 'primereact/dropdown';
import { find } from 'rxjs';
import { Button } from 'primereact/button';
import { IEntityType } from '../entity-types';
import { MessageServiceContext } from '../../messages';
import { CollectionPlaceholder } from '../../utils/placeholders';

interface PropertyListProps {
    properties: IProperty[];
    safe: boolean;
    parentUpdate: any;
    mode: "EntityType" | "LinkType";
}

const PropertyList: React.FC<PropertyListProps> = ({ properties, safe, parentUpdate, mode }) => {
    const {messageService} = useContext(MessageServiceContext);

    const dataTypesToIcons = DataTypes.ICONS_MAPPING();
    
    const [propertyCount, setPropertyCount] = useState(properties?.length);
    const [originalName, setOriginalName] = useState(""); // Need this because for some reason the text editor doesn't keep the value when editing started

    // Templates for icons

    const dataTypeBodyTemplate = (property : IProperty) => {
        if (property.dataType)
            return <FontAwesomeIcon icon={dataTypesToIcons[property.dataType]}size="lg"/>
        return <></>
    }

    const dataTypeSelectedTemplate = (option: any, props: any) => {
        return  <><div style={{height:"15px"}}></div></>
    }

    const dataTypeOptionTemplate = (option: any) => {
        return <FontAwesomeIcon icon={dataTypesToIcons[option.value]}size="lg"/>
    }

    const isKeyBodyTemplate = (property : IProperty) => {
        return <Checkbox disabled checked={property.key}></Checkbox>
    }

    const onRemovePropertyHandler = (name: string) => {
        return () => {
            console.log("here");
            let i = properties.findIndex(x => x.name === name);

            if (i >= 0) {
                properties.splice(i, 1);
                parentUpdate([...properties]);
                messageService?.show({severity:"success", summary:"Success", detail:'Property deleted'});
            }
        }
    }

    const getActionButtonsTemplate = (property : IProperty) => {
        if (!property.id)
            return <i className='pi pi-trash' style={{color: "red"}} onClick={onRemovePropertyHandler(property.name)}></i>;
        return <></>;
    }

    const onAddPropertyHandler = () => {
        const newProperty = "New Property";


        if (properties.findIndex(x => x.name === newProperty) >= 0){
            messageService?.show({severity:"error", summary:"Error", detail:`Can't have two properties with the same name ${newProperty}`});
            return;

        }

        parentUpdate([...properties,{name: newProperty, description: "A new property", dataType: DataTypes.STRING, key: false, inherited: false}]);
        messageService?.show({severity:"success", summary:"Success", detail:'Property added'});
    }
    

    const headerIcons = () => {
        return <Button icon='pi pi-plus' severity='success' rounded outlined style={{width:"25px", height:"25px"}}  onClick={onAddPropertyHandler}></Button>
    };

    // Templates for editing

    const textEditor = (options:any) => {
        return <InputText style={{width:"100%"}} type="text" value={options.value} onChange={(e) => {options.editorCallback(e.target.value)}} />;
    };

    const booleanEditor = (options:any) => {
        return <Checkbox checked={options.value} onChange={(e) => {options.editorCallback(e.checked)}}/>
    }

    const dataTypeEditor = (options:any) => {
        return <Dropdown style={{width:"50px"}}
            value={options.value} 
            options={DataTypes.TYPE_OPTIONS()} 
            onChange={(e) => {options.editorCallback(e.value)}}
            itemTemplate={dataTypeOptionTemplate}
            valueTemplate={dataTypeSelectedTemplate}
            />
    }

    
    const groupHeaderTemplate = (data: IProperty) => {
        return (
            <div className="flex align-items-center gap-2">
                {data.inherited && <span className="font-bold">Inherited</span>}
                {!data.inherited && <span className="font-bold">Non-inherited</span>}
            </div>
        );
    };

    // In this method we can validate all changes to a property
    const onCellEditComplete = (e: ColumnEvent) => {
        let { rowData, newValue, field, originalEvent: event } = e;
        
        if (rowData.inherited){ // Inherited properties of the parent can't be modified
            // TODO: BUG: When editing Name and Description I get the error toast twice
            messageService?.show({severity:"error", summary:"Error", detail:"Inherited properties can't be modified."});
            return;
        }

        if (field === "name"){
            if (!newValue){
                messageService?.show({severity:"error", summary:"Error", detail:"Name can't be empty"});
                rowData[field] = originalName;
                return;
            }


            if (properties.filter(x => x.name === newValue)?.length > 1){
                messageService?.show({severity:"error", summary:"Error", detail:"Can't have two properties with the same name"});
                rowData[field] = originalName;
                return;
            }
        }


        if (field === "key")
        {
            if (safe){
                messageService?.show({severity:"error", summary:"Error", detail:"Can't modify the keys of stored definitions."});
                return;
            }

            if (rowData.inherited) {
                messageService?.show({severity:"error", summary:"Error", detail:"Can't modify inherited keys."});
                return;
            }


        }

        if (field === "dataType"){
            
            if (rowData.id){ // The user can't modify the data type of a property that has already been saved
                messageService?.show({severity:"error", summary:"Error", detail:"Can't modify the data type of a property that has been saved before."});
                return;
            }
        }

        rowData[field] = newValue;
        if (field === "key"){
            parentUpdate([...properties]);
        }
    };

    if (properties.length > 0) {
        return (
            <Panel header="Properties" icons={headerIcons} className="property-list">
                <DataTable value={properties} editMode="cell" rowGroupMode="subheader" groupRowsBy='inherited' rowGroupHeaderTemplate={groupHeaderTemplate} sortField="inherited" scrollable scrollHeight="400px" tableStyle={{ lineHeight: '10px' }} style={{height:"100%"}}>
                  <Column field="dataType" header="" body={dataTypeBodyTemplate} style={{paddingRight: "2px", width: "20px"}} editor={(options) => dataTypeEditor(options)} onCellEditComplete={onCellEditComplete}></Column>
                  <Column field="name" header="Name" style={{width:"25%"}} editor={(options) => textEditor(options)} onCellEditComplete={onCellEditComplete} onCellEditInit={(e) => {setOriginalName(e.value)}}></Column>
                  <Column field="description" header="Description" style={{width: "60%"}} editor={(options) => textEditor(options)} onCellEditComplete={onCellEditComplete}></Column>
                  {
                    mode === "EntityType" && <Column field="key" header="Is Key" body={isKeyBodyTemplate} style={{ width: '12%' }} editor={(options) => booleanEditor(options)} onCellEditComplete={onCellEditComplete}></Column>
                  }
                  <Column field="" header="" body={getActionButtonsTemplate}/>
                </DataTable>
            </Panel>
        ); 
    } else {
        return (
            <Panel header="Properties" icons={headerIcons} className="property-list">
                <CollectionPlaceholder icon='pi pi-list' message=''></CollectionPlaceholder>
            </Panel>
        )
    }

};

export default PropertyList;
