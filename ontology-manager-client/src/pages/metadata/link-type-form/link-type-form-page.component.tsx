import { useContext, useEffect, useReducer, useState } from "react";
import { dependencyFactory } from "../../../features/shared/injection";
import { IEntityTypeService, ILinkTypeService, SERVICES } from "../../../services";
import { MessageServiceContext } from "../../../features/shared/messages";
import { useNavigate, useParams } from "react-router-dom";
import { ILinkType } from "../../../features/modules/metadata/link-types";
import { formReducer } from "../../../utils/formReducer";
import { IProperty, PropertyListEditorComponent } from "../../../features/modules/metadata/properties";
import { SelectableOption } from "../../../utils/selectable-option";
import { PageTitle } from "../../../components";
import { Panel } from "primereact/panel";
import { InputText } from "primereact/inputtext";
import { Dropdown } from "primereact/dropdown";
import { SelectButton } from "primereact/selectbutton";
import { InputTextarea } from "primereact/inputtextarea";
import { KeywordSearch } from "../../../features/modules/metadata/keywords";
import { Divider } from "primereact/divider";
import { Button } from "primereact/button";
import DatasetList from "../../../features/modules/metadata/data-sources/dataset-list.component";



const LinkTypeFormPageComponent: React.FC = () => {
    const entityService = dependencyFactory.get<IEntityTypeService>(SERVICES.IEntityTypeService);
    const linkTypeService = dependencyFactory.get<ILinkTypeService>(SERVICES.ILinkTypeService);
    const {messageService} = useContext(MessageServiceContext);
    const navigate = useNavigate();

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
            setLinkType(await linkTypeService.get(id, messageService!));
        
        setEntityTypeOptions(await entityService.getAllEntityTypesAsOptions(messageService!));

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
        await linkTypeService.create(linkType, messageService!).then(
            x => {
                navigate("/ontology/overview/")
            }
        );
    }


    return (
        <div className="container">
            <PageTitle icon='pi pi-arrows-h' title='Link Type Editor' help={true}/>



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

                            <KeywordSearch value={linkType.keywords} valueSetter={(e) => setLinkType({...linkType, keywords: e.target.value})}></KeywordSearch>
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
                    <PropertyListEditorComponent properties={linkType.properties} safe={false} parentUpdate={updateProperty} mode="LinkType"/>
                </div>
                <div className="col-md-4">
                    <DatasetList></DatasetList>
                </div>
            </div>
        </div>
    );
};

export default LinkTypeFormPageComponent;
