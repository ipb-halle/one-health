import { useContext, useEffect, useState } from "react";
import { StructureFilterMatchMode } from "../../../features/filters/enums/structure-filter-match-mode";
import { CollectionPlaceholderComponent, LoadingPlaceholderComponent, PageTitle } from "../../../components";
import { Panel } from "primereact/panel";
import StructureEditor from "../../../features/filters/structure-editor.component";
import { Dropdown } from "primereact/dropdown";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import { dependencyFactory } from "../../../features/shared/injection";
import { ICompoundService, SERVICES } from "../../../services";
import { MessageServiceContext } from "../../../features/shared/messages";
import { SplitButton } from "primereact/splitbutton";
import { FileUpload, FileUploadHandlerEvent, FileUploadSelectEvent, FileUploadUploadEvent } from "primereact/fileupload";
import { Paginator, PaginatorPageChangeEvent } from "primereact/paginator";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { InputNumber } from "primereact/inputnumber";
import { Slider } from "primereact/slider";
import { ProgressSpinner } from "primereact/progressspinner";
import { faL } from "@fortawesome/free-solid-svg-icons";
import { RadioButton } from "primereact/radiobutton";
import { InputTextarea } from "primereact/inputtextarea";
import { INeighborhoodExplorerStore } from "../../../stores/neighborhood-explorer-store";
import { STORES } from "../../../stores";
import { useNavigate } from "react-router-dom";
const OpenChemLib = require("openchemlib/full");


export interface CompoundSearchQuery {
    value?: string;
    matchMode?: string;
    threshold?: any;
}

export interface ExactSearchQuery {
    value?: string;
    matchMode?: string;
    inchi?: string;
    inchikey?: string;
    smiles?: string;
}


export const CompoundSearchPageComponent: React.FC = () => {
    const navigate = useNavigate();

    const maxResultsOptions = [50, 100, 150, 200, 250, 500, 1000].map(x => { return { label: x, value: x }; });
    const structureFilterMatchModes = Object.entries(StructureFilterMatchMode).map(([key, value]) => { return { label: value, value: key }; });

    const  neighborhoodExplorerStore = dependencyFactory.get<INeighborhoodExplorerStore>(STORES.INeighborhoodExplorerStore);
    const compoundService = dependencyFactory.get<ICompoundService>(SERVICES.ICompoundService);
    const {messageService} = useContext(MessageServiceContext);

    const [first, setFirst] = useState<number>(0);
    const [rows, setRows] = useState<number>(50);
    const [total, setTotal] = useState<number>(0);

    const [loading, setLoading] = useState<boolean>(false);

    const [editor, setEditor] = useState<any>();

    const [items, setItems] = useState<any[]>([]);

    const [structureQuery, setStructureQuery] = useState<CompoundSearchQuery>({threshold: 80});
    const [exactQuery, setExactQuery] = useState<ExactSearchQuery>({});
    const [selectedCompounds, setSelectedCompounds] = useState<any[]>([]);

    useEffect(() => {
        let newEditor = OpenChemLib.StructureEditor.createSVGEditor("structureSearchEditor", 1);
        setEditor(newEditor);
        // metadataService.getBySMILES("[H]OC(=O)C=1C(OC(=O)C([H])([H])[H])=C([H])C([H])=C([H])C1[H]", messageService!);

    }, [])

    const onMolFileUpload = async (e: FileUploadSelectEvent) => {

        if (e.files.length <= 0)
            return;

        const molFile = await e.files[0].text();


        editor.setMolFile(molFile);
        
    
        messageService!.show({ severity: 'success', summary: 'Success', detail: 'File Uploaded' });
    };

    const chooseOptions = { icon: 'pi pi-fw pi-upload'  };

    const onPageChange = (e:PaginatorPageChangeEvent) => {
        setFirst(e.first);
        setRows(e.rows);
    }

    const onStructureSearch = async () => {
        const smiles = editor.getSmiles();

        console.log(smiles);

        setSelectedCompounds([]);
        
        if (!smiles) messageService?.show({severity: 'error', summary: 'Error!', detail: 'Empty structure'})

        setFirst(0);
        const newStructureQuery = {...structureQuery, value: smiles};
        setStructureQuery(newStructureQuery);
        
        if (structureQuery.matchMode === "SUBSTRUCTURE"){
            await runSubstructureSearch(newStructureQuery);    
        } else if (structureQuery.matchMode === "TAN_SIMILARITY"){
            await runSimilaritySearch(newStructureQuery);
        } else if (structureQuery.matchMode === "EXACT_SMILES"){
            await runSMILESSearch(newStructureQuery.value);
        }

    }

    const onExactSearch = async () => {
        if (!exactQuery.matchMode)
            return;

        if (exactQuery.matchMode === "smiles"){
            runSMILESSearch(exactQuery.smiles!);
        } else if (exactQuery.matchMode === "inchi"){
            runInChISeach(exactQuery.inchi!);
        } else if (exactQuery.matchMode === "inchikey"){
            runInChIKeySeach(exactQuery.inchikey!);
        }
    } 




    const runSubstructureSearch = async (structureQuery:any) => {
        setLoading(true);
        const page = await compoundService.getBySubstructure(structureQuery.value!, rows, first, messageService!);
        setLoading(false);
        setTotal(page.total);
        setItems(page.items);
    }

    const runSimilaritySearch = async (structureQuery:any) => {
        setLoading(true);
        const page = await compoundService.getBySimilarity(structureQuery.value!, structureQuery.threshold, rows, messageService!);
        setLoading(false);
        setTotal(page.total);
        setItems(page.items);
    }

    const runSMILESSearch = async (value:string) => {
        const element = await compoundService.getBySMILES(value, messageService!);
        if (element){
            setTotal(1);
            setItems([element]);
        }
        else {
            setTotal(0);
            setItems([]);
        }
    }

    const runInChISeach = async (value:string) => {
        const element = await compoundService.getByInChI(value, messageService!);
        if (element){
            setTotal(1);
            setItems([element]);
        }
        else {
            setTotal(0);
            setItems([]);
        }

    }

    const runInChIKeySeach = async (value:string) => {
        const element = await compoundService.getByInChIKey(value, messageService!);
        if (element){
            setTotal(1);
            setItems([element]);
        }
        else {
            setTotal(0);
            setItems([]);
        }

    }


    const structureDrawTemplate = (compound:any) => {
        return <>
            <MolecularDrawComponent element={compound}/>
        </>
    }

    return <div className="container">
        <PageTitle icon='fa fa-atom' title='Compound Search' help={true} />

        <div className="row" style={{ marginBottom: 20 }}>
            <div className="col-6">
                <Panel header="Search by structure">
                    <div style={{ height: '540px' }}>
                        <div className="row" style={{marginBottom: 10, paddingRight: "20px"}}>
                            <div className="col-3">
                                <FileUpload  
                                    mode="basic" 
                                    name="demo[]" 
                                    url="/api/upload" 
                                    auto 
                                    customUpload={true} 
                                    accept=".mol"
                                    maxFileSize={1000000} 
                                    uploadHandler={(e:FileUploadHandlerEvent) => {e.options.clear()}}  
                                    onSelect={onMolFileUpload} 
                                    chooseLabel="Load file"  
                                    chooseOptions={chooseOptions}
                                />
                                {/* <SplitButton label="Load" icon="pi pi-upload" model={uploadOptions} /> */}
                            </div>
                            <div className="col-2">

                            </div>

                            <div className="col-7">

                                <Dropdown
                                    style={{ width: "350px" }}
                                    options={structureFilterMatchModes}
                                    placeholder="Select structure match mode..."
                                    value={structureQuery.matchMode}
                                    onChange={x => setStructureQuery({ ...structureQuery, matchMode: x.value })}
                                >
                                </Dropdown>
                            </div>


                        </div>
                        <div style={{ height: "450px", paddingBottom: "10px" }}>

                            <div style={{width: "100%", height: "100%", border: "1px solid #DEE2E6"}} id="structureSearchEditor"/>
                        </div>
                        <div className="row" style={{ paddingRight: "5px" }}>
                            
                            <div className="col-4">
                                <label>Max results:</label>&nbsp;
                                <Dropdown
                                    style={{ width: '85px' }}
                                    options={maxResultsOptions}
                                    value={rows}
                                    onChange={x => setRows(x.value)}>
                                </Dropdown>
                            </div>
                            <div className="col-3">
                                {
                                    structureQuery.matchMode === "TAN_SIMILARITY" && <>
                                    
                                    <InputNumber 
                                    value={structureQuery.threshold} 
                                    onChange={(e) => setStructureQuery({...structureQuery, threshold: e.value})}
                                    min={0}
                                    max={100}
                                    suffix="% similar"
                                     />
                                <Slider 
                                    style={{width: "193px"}}  
                                    value={structureQuery.threshold} 
                                    onChange={(e:any) => setStructureQuery({...structureQuery, threshold: e.value})} 
                                    min={0}
                                    max={100}
                                />
                                    </>
                                }
                             
                            </div>
                            <div className="col-3">

                            </div>

                            <div className="col-2" >

                                <Button
                                    style={{ marginRight: 5 }}
                                    label="Search"
                                    onClick={onStructureSearch}
                                    >

                                </Button>

                            </div>

                        </div>
                    </div>

                </Panel>
            </div>
            <div className="col-6">
                <Panel header="Search by attribute (exact match)">
                    <div style={{ height: '540px' }}>
                        <div style={{ height: '500px' }}>



                            <div className="form-group" style={{paddingBottom: '10px'}}>
                                <div style={{display: 'flex', alignItems: 'center', marginBottom: '2px'}}>
                                    <RadioButton inputId="smilesRadioButton" value="smiles" onChange={(e) => setExactQuery({...exactQuery, matchMode: e.value})} checked={exactQuery.matchMode === 'smiles'} />
                                    <label htmlFor="smilesRadioButton" style={{marginLeft: "4px"}}>SMILES</label>
                                </div>
                                <InputTextarea
                                    disabled={exactQuery.matchMode !== "smiles"}
                                    value={exactQuery.smiles}
                                    onChange={(e) => {setExactQuery({...exactQuery, smiles: e.target.value})}}
                                    style={{resize: 'none'}}
                                    className="form-control"
                                    id="exactSearchQuery.smiles" />
                            </div>

                            <div className="form-group" style={{paddingBottom: '10px'}}>
                                <div style={{display: 'flex', alignItems: 'center', marginBottom: '2px'}}>
                                    <RadioButton inputId="inchiRadioButton" value="inchi" onChange={(e) => setExactQuery({...exactQuery, matchMode: e.value})} checked={exactQuery.matchMode === 'inchi'} />
                                    <label htmlFor="inchiRadioButton" style={{marginLeft: "4px"}}>InChI</label>
                                </div>
                                <InputTextarea
                                    disabled={exactQuery.matchMode !== "inchi"}
                                    value={exactQuery.inchi}
                                    onChange={(e) => {setExactQuery({...exactQuery, inchi: e.target.value})}}
                                    style={{resize: 'none'}}
                                    className="form-control"
                                    id="exactSearchQuery.inchi" />
                            </div>

                            <div className="form-group" style={{paddingBottom: '10px'}}>
                                <div style={{display: 'flex', alignItems: 'center', marginBottom: '2px'}}>
                                    <RadioButton inputId="inchikeyRadioButton" value="inchikey" onChange={(e) => setExactQuery({...exactQuery, matchMode: e.value})} checked={exactQuery.matchMode === 'inchikey'} />
                                    <label htmlFor="inchikeyRadioButton" style={{marginLeft: "4px"}}>InChI Key</label>
                                </div>
                                <InputText
                                    disabled={exactQuery.matchMode !== "inchikey"}
                                    value={exactQuery.inchikey}
                                    onChange={(e) => {setExactQuery({...exactQuery, inchikey: e.target.value})}}
                                    className="form-control"
                                    id="exactSearchQuery.inchikey" />
                            </div>
                        </div>
                        <div className="row" style={{ paddingRight: "5px" }}>
                            <div className="col-10">

                            </div>
                            <div className="col-2" style={{ paddingRight: 5 }}>

                                <Button
                                    style={{ marginRight: 5 }}
                                    label="Search"
                                    onClick={onExactSearch}
                                    >

                                </Button>

                            </div>

                        </div>

                    </div>

                </Panel>
            </div>

        </div>

            <div style={{border: '1px solid #DEE2E6'}}>
                <div style={{borderBottom:'1px solid #DEE2E6', padding: "5px", display: 'flex', alignItems: 'center' }}>

                    <Button 
                        icon="fa fa-compass" 
                        tooltip="Show selected records in neighborhood explorer" 
                        tooltipOptions={{showDelay: 800, position: 'bottom'}}
                        onClick={() => {
                            neighborhoodExplorerStore.setIds(selectedCompounds.map(x => { return {id: x.id, color: "#343ea0", label: x.name }}));
                            navigate('/neighborhood-explorer');
                        }}
                    ></Button>
                    <span style={{marginLeft: 10}}>{items.length > 0 ? `Showing ${items.length} results` : "No results"}</span>
                    
                </div>
                {/* <CollectionPlaceholderComponent icon='pi pi-list' message=''/> */}
                {
                    loading && <LoadingPlaceholderComponent></LoadingPlaceholderComponent>
                }{
                    !loading &&   
                    <DataTable 
                        scrollable 
                        scrollHeight="500px"
                        selectionMode="multiple"
                        metaKeySelection={false}
                        selection={selectedCompounds}
                        onSelectionChange={(e) => setSelectedCompounds(e.value)}
                        value={items} 
                        tableStyle={{ minWidth: '50rem' }}>
                    <Column field="" header="Structure" body={structureDrawTemplate}></Column>
                    <Column field="smiles" header="SMILES"></Column>
                    <Column field="molecularFormula" header="Mol. Formula"></Column>
                    <Column field="molecularWeight" header="Mol. Weight"></Column>
                </DataTable>
                }
              
            </div>

    </div>;
};


interface MolecularDrawComponentProps {
    element:any
}

const MolecularDrawComponent: React.FC<MolecularDrawComponentProps> = ({element}) => {
    
    const [hidden, setHidden] = useState<boolean>(false);
    
    useEffect(() => {
        const smiles = element.smiles;
        console.log(smiles);

        if (smiles){
            setHidden(false);
            const canvas = document.getElementById(`molecular-draw-${element.id}`) as HTMLCanvasElement;
            const molecule = OpenChemLib.Molecule.fromSmiles(smiles);
        
            console.log("cojoneee aquiiii");
            OpenChemLib.StructureView.drawMolecule(canvas, molecule);
            // renderer.draw(molecule, 'molecular-draw');
        } else {
            setHidden(true);
        }
    }, [element])


    return (
        <canvas id={`molecular-draw-${element.id}`} hidden={hidden}></canvas>
    );
};


export default CompoundSearchPageComponent;