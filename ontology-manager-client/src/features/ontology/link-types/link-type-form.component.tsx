import { Card, Form, Row, Col } from 'react-bootstrap';
import { PropertyList } from '../properties';
import { IProperty } from '../properties';
import { DataTypes } from '../data-types/data-types';

import { Panel } from 'primereact/panel';
import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';
import { InputText } from 'primereact/inputtext';
import { Dropdown } from 'primereact/dropdown';
import { InputTextarea } from 'primereact/inputtextarea';
import { MultiSelect } from 'primereact/multiselect';
import { SelectButton } from 'primereact/selectbutton';
import { useReducer, useState } from 'react';
import { Toast } from 'primereact/toast';
import { useRef } from 'react';
import { RefObject } from 'react';
import { PageTitle } from '../../layout';
import { formReducer } from '../../utils/formReducer';
import { ILinkType } from './link-type';
import { dependencyFactory } from '../../injection/inversify.config';
import { IEntityTypeService, ILinkTypeService } from '../../services';
import { SERVICE_TYPES } from '../../services';
import { SelectableOption } from '../../utils/selectable-option';
import { useEffect } from 'react';
import { KeywordSearch } from '../keywords';
import { faL } from '@fortawesome/free-solid-svg-icons';
import { useParams } from 'react-router-dom';
import DatasetList from '../data-sources/dataset-list.component';
const React = require('react');



const LinkTypeForm: React.FC = () => {
    const entityService = dependencyFactory.get<IEntityTypeService>(SERVICE_TYPES.IEntityTypeService);
    const linkTypeService = dependencyFactory.get<ILinkTypeService>(SERVICE_TYPES.ILinkTypeService);
    const toast : RefObject<Toast> = useRef<Toast>(null);
    const {id} = useParams();

    const directionOptions = [{name: "Outgoing", value: "OUTGOING"}, {name: "Incoming", value: "INCOMING"}, {name: "Undirected", value: "UNDIRECTED"}];
    
    const [linkType, setLinkType] = useReducer(
        formReducer<ILinkType>,
        {
            name: "",
            direction: "",
            leftEntityTypeId: "",
            leftCardinality: "",
            rightEntityTypeId: "",
            rightCardinality: "",
            description: "",
            keywords: [],
            properties: []
        }
    )

    const [entityTypeOptions, setEntityTypeOptions] = useState<SelectableOption[]>([]);
    const [definitionValid, setDefinitionValid] = useState<boolean>(false);

    const init = async (id: string | undefined) => {
        if (id)
            setLinkType(await linkTypeService.get(id, toast));
        
        setEntityTypeOptions(await entityService.getAllEntityTypesAsOptions(toast));

    };

    useEffect(() => {
        init(id);
    }, 
    
    []);


    const updateProperty = (properties: IProperty[]) => {
        return setLinkType({...linkType, properties: properties});
    }

    const checkDefinitionValidity = () => {
        if (!linkType.name || !linkType.direction || !linkType.leftEntityTypeId || !linkType.leftCardinality || !linkType.rightEntityTypeId || !linkType.rightCardinality)
            return false

        return true;
    }

    useEffect(() => {
        setDefinitionValid(checkDefinitionValidity());
     }, [linkType.name, linkType.direction, linkType.leftEntityTypeId, linkType.leftCardinality, linkType.rightEntityTypeId, linkType.rightCardinality])
    

    const onSaveHandler = async () => {
        console.log(linkType);
        const newLinkType = await linkTypeService.create(linkType, toast);

        setLinkType(linkType);
    }


    return (
        <div className="container">
            <Toast ref={toast}></Toast>
            <PageTitle icon='pi pi-arrows-h' title='Link Type Editor'/>



            <Panel header="Definition" className="mb-4">
                <div className="row mb-4">


                    <div className="col-md-4">
                        <div className="form-group">
                            <label htmlFor="linkType.name" className="font-bold block mb-2 asterisk-mark">Name</label>
                            <InputText 
                                value={linkType.name}
                                className="form-control"
                                id="linkType.name"
                                onChange={(e) => setLinkType({...linkType, name: e.target.value})}
                            />
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="form-group">
                            <label htmlFor="linkType.direction" className="font-bold block mb-2 asterisk-mark"> Direction</label>
                            <Dropdown
                                className="w-100"
                                optionLabel="name"
                                optionValue="value"
                                value={linkType.direction}
                                placeholder="Select a direction"
                                options={directionOptions}
                                onChange={(e) => setLinkType({...linkType, direction: e.target.value})}
                            />
                        </div>
                    </div>

                </div>

                <div className="row mb-4">

                    <div className="col-md-4">
                        <div className="form-group">
                            <label htmlFor="linkType.leftEntityTypeId" className="font-bold block mb-2 asterisk-mark">Left entity type</label>
                            <Dropdown
                                className="w-100"
                                placeholder="Select a type"
                                filter
                                options={entityTypeOptions}
                                value={linkType.leftEntityTypeId}
                                onChange={(e) => setLinkType({...linkType, leftEntityTypeId: e.target.value})}
                            />
                        </div>
                    </div>

                    <div className="col-md-2">
                        <div className="form-group">
                            <label htmlFor="linkType.leftCardinality" className="font-bold block mb-2 asterisk-mark">Left Cardinality</label>

                            <SelectButton
                                value={linkType.leftCardinality}
                                onChange={(e) => setLinkType({...linkType, leftCardinality: e.value})}
                                options={[
                                    { label: 'One', value: 'ONE' },
                                    { label: 'Many', value: 'MANY' },
                                ]}
                            />
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="form-group">
                            <label htmlFor="linkType.rightEntityTypeId" className="font-bold block mb-2 asterisk-mark">Right entity type</label>

                            <Dropdown
                                  className="w-100"
                                  placeholder="Select a type"
                                  filter
                                  options={entityTypeOptions}
                                  value={linkType.rightEntityTypeId}
                                  onChange={(e) => setLinkType({...linkType, rightEntityTypeId: e.target.value})}
                            />
                        </div>
                    </div>


                    <div className="col-md-2">
                        <div className="form-group">
                            <label htmlFor="linkType.rightCardinality" className="font-bold block mb-2 asterisk-mark">Right Cardinality</label>

                            <SelectButton
                                value={linkType.rightCardinality}
                                onChange={(e) => setLinkType({...linkType, rightCardinality: e.value})}
                                options={[
                                    { label: 'One', value: 'ONE' },
                                    { label: 'Many', value: 'MANY' },
                                ]}
                            />
                        </div>
                    </div>
                </div>

                <div className="row">

                    <div className="col-md-6">
                        <div className="form-group">
                            <label htmlFor="linkType.description" className="font-bold block mb-2">Description</label>
                            <InputTextarea 
                                className="form-control" 
                                rows={3} 
                                value={linkType.description}
                                onChange={(e) => setLinkType({...linkType, description: e.target.value})}
                                />
                        </div>
                    </div>

                    <div className="col-md-6">
                        <div className="form-group">
                            <label htmlFor="linkType.keywords"  className="font-bold block mb-2" >Keywords</label>

                            <KeywordSearch value={linkType.keywords} valueSetter={(e) => setLinkType({...linkType, keywords: e.target.value})}  toast={toast}></KeywordSearch>
                        </div>
                    </div>
                </div>

                <Divider />

                <div className="row justify-content-end px-3">
                    <div className="col" style={{ maxWidth: '80px' }}>
                        <Button label="Save" severity="success" disabled={!definitionValid} onClick={onSaveHandler} />
                    </div>
                    <div className="col" style={{ maxWidth: '100px' }}>
                        <Button label="Cancel" severity="danger" />
                    </div>
                </div>

            </Panel>

             <div className="row">
                <div className="col-md-8" style={{height: "430px"}}>
                    <PropertyList properties={linkType.properties} safe={false} parentUpdate={updateProperty} mode="LinkType"></PropertyList>
                </div>
                <div className="col-md-4">
                    <DatasetList></DatasetList>
                </div>
            </div>
        </div>
    );
};

export default LinkTypeForm;
