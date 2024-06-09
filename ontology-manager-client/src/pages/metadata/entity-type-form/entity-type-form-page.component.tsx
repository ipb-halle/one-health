import { useContext, useEffect, useReducer, useRef, useState } from "react";
import { dependencyFactory } from "../../../features/shared/injection";
import { MessageServiceContext } from "../../../features/shared/messages";
import { IEntityTypeService, SERVICES } from "../../../services";
import { useNavigate, useParams } from "react-router-dom";
import { IEntityType } from "../../../features/modules/metadata/entity-types";
import { formReducer } from "../../../utils/formReducer";
import { SelectableOption } from "../../../utils/selectable-option";
import { IProperty, PropertyListEditorComponent } from "../../../features/modules/metadata/properties";
import EntityTypeFormTour from "../../../features/modules/metadata/entity-types/entity-type-form.tour.component";
import { PageTitle } from "../../../components";
import { Panel } from "primereact/panel";
import { ColorPicker } from "primereact/colorpicker";
import { InputText } from "primereact/inputtext";
import { Dropdown } from "primereact/dropdown";
import { InputTextarea } from "primereact/inputtextarea";
import { KeywordSearch } from "../../../features/modules/metadata/keywords";
import { Button } from "primereact/button";
import { Divider } from "primereact/divider";
import DatasetList from "../../../features/modules/metadata/data-sources/dataset-list.component";
import { Messages } from "primereact/messages";
import { useMountEffect } from 'primereact/hooks';

const EntityTypeFormPageComponent: React.FC = () => {
    // Services Initialization
    const entityService = dependencyFactory.get<IEntityTypeService>(SERVICES.IEntityTypeService);
    const {messageService} = useContext(MessageServiceContext);
    const navigate = useNavigate();

    const msgs = useRef<Messages>(null);
    
    // Component State
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
    const [runTutorial, setRunTutorial] = useState<boolean>(false);

    const updateProperty = (properties: IProperty[]) => {
        return setEntityType({...entityType, properties: properties});
    }


    const init = async (id: string | undefined) => {
        if (id){
            setEntityType(await entityService.get(id, messageService!));
        }
        
            //TODO: no mapping needed here
        setParentOptions(
            (await entityService.getAllEntityTypesAsOptions(messageService!))
                .map(x => { return {...x, disabled: x.value === entityType.id}}) // stops the user from selecting the same type as the parent
        );

    };

    const handleParentUpdate = async () => {
        entityType.properties = entityType.properties?.filter(x => !x.inherited);

        if (entityType.parent?.id){
            const parent = await entityService.get(entityType.parent.id, messageService!);
            setEntityType({ ...entityType, properties: [...entityType.properties, ...parent.properties.map(x => { return {...x, inherited: true} } )] });
        }

    }

    useEffect(() => {
        init(id);
    }, 
    
    []);

    useMountEffect(() => {
        if (msgs.current) {
            msgs.current.clear();
            msgs.current.show([
                {
                    severity: 'error',
                    sticky: true,
                    content: (
                        <p>
                            This feature is currently in beta and is not fully stable. While we encourage you to explore and provide feedback, please be aware that using this feature will lead to bugs, crashes, or unexpected behavior.
                        </p>
                    )
                }
            ]);
        }
    }); 

    useEffect(() => {
       handleParentUpdate();
    }, [entityType.parent?.id])

    useEffect(() => {
        setDefinitionValid(checkDefinitionValidity());

    }, [entityType.name, entityType.pluralName])

    useEffect(() => {
        setPropertiesValid(checkPropertiesValidity());

    }, [entityType.properties])

    const checkDefinitionValidity = () => {
        if (!entityType.name || !entityType.pluralName)
            return false

        return true;
    }

    const checkPropertiesValidity = () => {
        if (entityType.properties.length === 0){
            // messageService!.current?.show({severity:"error", summary:"Error", detail:"An entity type should have at least one property."});
            return false;
        }
    
        if (entityType.properties.findIndex(x => x.key) < 0){
            // messageService!.current?.show({severity:"error", summary:"Error", detail:"An entity type should have at least one key."});
            return false;
        }
        
        return true;
    }


   

    const onSaveHandler = async () => {
        await entityService.create(entityType, messageService!).then(
            x => {
                navigate("/ontology/overview/")
            }
        );

        setEntityType(entityType);
    }

    const helpClickedHandler = () => {
        setRunTutorial(true);
    }

    const helpTourCallback = () => {
        setRunTutorial(false);
    }



    return (
        
        <div className="page-container-narrow" style={{pointerEvents: 'none', cursor: 'not-allowed'}}>
            <EntityTypeFormTour run={runTutorial} callback={helpTourCallback}/>

            <PageTitle icon='pi pi-box' title='Entity Type Editor' help={true} helpClickedHandler={helpClickedHandler}/>
            <Messages ref={msgs} />
        
            <Panel id="definition" header="Definition" className="mb-4">
                <div className="row mb-4">

                    <div className='col-md-1'>
                        <div className="form-group">
                            <label htmlFor='entityType.color' className="font-bold block mb-2">Icon</label>
                            <div style={{width:"42px", height:"42px", border:"solid 1px black"}}>

                            </div>
                        </div>
                    </div>

                    <div className='col-md-1'>
                        <div className="form-group">
                            <label htmlFor='entityType.color' className="font-bold block mb-2">Color</label>
                            <div></div>
                            <ColorPicker 
                                value={entityType.color} 
                                onChange={(e:any) => setEntityType({...entityType, color: e.value ? e.value : "#F36924"} )}
                                />
                        </div>
                    </div>

                    <div className="col-md-3">
                        <div className="form-group">
                            <label htmlFor='entityType.name' className="font-bold block mb-2 asterisk-mark">Name</label>
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
                                style={{resize: 'none'}}
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
                            <KeywordSearch value={entityType.keywords} valueSetter={(e) => setEntityType({...entityType, keywords: e.target.value})}></KeywordSearch>
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
                <div id="properties" className="col-md-8" style={{height: "500px"}}>
                    <PropertyListEditorComponent properties={entityType.properties} safe={false} parentUpdate={updateProperty} mode="EntityType"/>
                </div>
                <div className="col-md-4" style={{height: "500px"}}>
                    <DatasetList></DatasetList>
                </div>
            </div>
        </div>
    );
};

export default EntityTypeFormPageComponent;

