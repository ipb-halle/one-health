import React, { RefObject, useEffect, useReducer } from 'react';
import PropertyList from '../properties/property-list.component';
import { Dropdown } from 'primereact/dropdown';


import { IProperty } from '../properties';
import { DataTypes } from '../data-types/data-types';
import { Panel } from 'primereact/panel';
import { InputText } from 'primereact/inputtext';

import { InputTextarea } from 'primereact/inputtextarea';
import { MultiSelect } from 'primereact/multiselect';
import { string } from 'yargs';
import { useState, useRef } from 'react';
// import DatasetList from '../datasets/dataset-list.component';
import { Divider } from 'primereact/divider';
import { Button } from 'primereact/button';

// Dependency injection
import { IEntityTypeService } from '../../services';
import { SERVICE_TYPES } from '../../services';
import { dependencyFactory } from '../../injection/inversify.config';


import { Toast } from 'primereact/toast';
import { formReducer } from '../../utils/formReducer';
import { PageTitle } from '../../layout';
import { IEntityType } from './entity-type';
import { EntityName } from 'typescript';
import { SelectableOption } from '../../utils/selectable-option';
import { IKeywordService } from '../../services/keyword-service';
import { IKeyword } from '../keywords';
import {KeywordSearch} from '../keywords';
import { useParams } from 'react-router-dom';
import { ColorPicker } from 'primereact/colorpicker';
import DatasetList from '../data-sources/dataset-list.component';


const EntityTypeForm: React.FC = () => {
    const entityService = dependencyFactory.get<IEntityTypeService>(SERVICE_TYPES.IEntityTypeService);
    const toast : RefObject<Toast> = useRef<Toast>(null);
    const {id} = useParams();

    
    const [entityType, setEntityType] = useReducer(
        formReducer<IEntityType>,
        {   // default state for new entity typ
            name: "",
            pluralName: "",
            parent: undefined,
            description: "",
            keywords: [],
            properties: [],
            color: "#F36924",
        }
    )

    const [parentOptions, setParentOptions] = useState<SelectableOption[]>([]);
    const [definitionValid, setDefinitionValid] = useState<boolean>(false);
    const [propertiesValid, setPropertiesValid] = useState<boolean>(false);

    

    const updateProperty = (properties: IProperty[]) => {
        return setEntityType({...entityType, properties: properties});
    }

    


    const init = async (id: string | undefined) => {
        if (id)
            setEntityType(await entityService.get(id, toast));
        
            //TODO: no mapping needed here
        setParentOptions(
            (await entityService.getAllEntityTypesAsOptions(toast))
                .map(x => { return {...x, disabled: x.value === entityType.id}}) // stops the user from selecting the same type as the parent
        );

    };

    const handleParentUpdate = async () => {
        console.log("updating parent of entity type")
        entityType.properties = entityType.properties?.filter(x => !x.inherited);

        if (entityType.parent?.id){
            const parent = await entityService.get(entityType.parent.id, toast);
            setEntityType({ ...entityType, properties: [...entityType.properties, ...parent.properties.map(x => { return {...x, inherited: true} } )] });
        }

        console.log(entityType);
    }

    useEffect(() => {
        init(id);
    }, 
    
    []);

    useEffect(() => {
       handleParentUpdate();
    }, [entityType.parent?.id])

    useEffect(() => {
        setDefinitionValid(checkDefinitionValidity());
        console.log(definitionValid);

    }, [entityType.name, entityType.pluralName])

    useEffect(() => {
        setPropertiesValid(checkPropertiesValidity());
        console.log(propertiesValid);

    }, [entityType.properties])

    const checkDefinitionValidity = () => {
        if (!entityType.name || !entityType.pluralName)
            return false

        return true;
    }

    const checkPropertiesValidity = () => {
        if (entityType.properties.length === 0){
            // toast.current?.show({severity:"error", summary:"Error", detail:"An entity type should have at least one property."});
            return false;
        }
    
        if (entityType.properties.findIndex(x => x.key) < 0){
            // toast.current?.show({severity:"error", summary:"Error", detail:"An entity type should have at least one key."});
            return false;
        }
        
        return true;
    }


   

    const onSaveHandler = async () => {
        console.log(entityType);
        const newEntityType = await entityService.create(entityType, toast);

        setEntityType(entityType);
    }


    return (
        <div className="container">
            <Toast ref={toast}></Toast>
            <PageTitle icon='pi pi-box' title='Entity Type Editor'/>
        
            <Panel header="Definition" className="mb-4">
                <div className="row mb-4">

                <div className='col-md-1'>
                    <div className="form-group">
                            <label htmlFor='entityType.color' className="font-bold block mb-2">Icon</label>
                            <div style={{width:"50px", height:"50px", border:"solid 1px black"}}>

                            </div>
                        </div>
                </div>

                    <div className='col-md-1'>
                    <div className="form-group">
                            <label htmlFor='entityType.color' className="font-bold block mb-2">Color</label>
                            <div></div>
                            <ColorPicker value={entityType.color} onChange={(e:any) => setEntityType({...entityType, color: e.value ? e.value : "#F36924"} )}/>
                        </div>
                    </div>

                    <div className="col-md-3">
                        <div className="form-group">
                            <label htmlFor='entityType.name' className="font-bold block mb-2 asterisk-mark">Name {entityType.name}</label>
                            <InputText 
                                className= "form-control" 
                                id = "entityType.name"
                                value={entityType.name}
                                onChange={(e) => setEntityType({...entityType, name: e.target.value})}
                                // keyfilter={/^[+]?(d{1,12})?$/} 
                                // validateOnly
                            />
                        </div>
                    </div>

                    <div className="col-md-3">
                        <div className="form-group">
                            <label htmlFor='entityType.name' className="font-bold block mb-2 asterisk-mark">Plural name</label>
                            <InputText
                                className="form-control"
                                id="entityType.pluralName"
                                value={entityType.pluralName}
                                onChange={(e) => setEntityType({...entityType, pluralName: e.target.value})}
                                // keyfilter={/^[+]?(d{1,12})?$/}
                                // validateOnly
                            />
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="form-group">
                            <label htmlFor="entityType.parent.id" className="font-bold block mb-2">Parent type</label>
                            <Dropdown className="w-100"
                                disabled={!!entityType.id}
                                options={parentOptions}
                                placeholder="Select a type"
                                optionDisabled="disabled"
                                value={entityType.parent?.id}
                                onChange={(e) => setEntityType({...entityType, parent: {id: e.target.value}})}
                                filter
                            />
                        </div>
                    </div>

                </div>

                <div className="row">
                    
                    <div className="col-md-6">
                        <div className="form-group">
                            <label htmlFor="entityType.description" className="font-bold block mb-2">Description</label>
                            <InputTextarea 
                                className="form-control" 
                                rows={3} 
                                id="entityType.description"
                                value={entityType.description}
                                onChange={(e) => setEntityType({...entityType, description: e.target.value})}
                            />
                        </div>
                    </div>


                    <div className="col-md-6">
                        <div className="form-group">
                            <label htmlFor="entityType.keywords" className="font-bold block mb-2">Keywords</label>
                            <KeywordSearch value={entityType.keywords} valueSetter={(e) => setEntityType({...entityType, keywords: e.target.value})}  toast={toast}></KeywordSearch>
                        </div>
                    </div>
                </div>

                <Divider />


                <div className="row justify-content-end px-3">
                    <div className="col" style={{ maxWidth: '80px' }}>
                        <Button label="Save" disabled={!(definitionValid && propertiesValid)} onClick={onSaveHandler} severity="success" />
                    </div>
                    <div className="col" style={{ maxWidth: '100px' }}>
                        <Button label="Cancel" severity="danger" />
                    </div>
                </div>
            </Panel>

            <div className="row">
                <div className="col-md-8" style={{height: "430px"}}>
                    <PropertyList properties={entityType.properties} safe={false} parentUpdate={updateProperty} mode="EntityType"></PropertyList>
                </div>
                <div className="col-md-4">
                    <DatasetList></DatasetList>
                </div>
            </div>
        </div>
    );
};

export default EntityTypeForm;
