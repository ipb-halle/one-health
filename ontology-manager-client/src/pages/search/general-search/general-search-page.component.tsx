import { ReactNode, useContext, useEffect, useState } from "react";
import { StructureFilterMatchMode } from "../../../features/filters/enums/structure-filter-match-mode";
import { CollectionPlaceholderComponent, LoadingPlaceholderComponent, PageTitle } from "../../../components";
import { Panel } from "primereact/panel";
import StructureEditor from "../../../features/filters/structure-editor.component";
import { Dropdown } from "primereact/dropdown";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { MessageServiceContext } from "../../../features/shared/messages";
import { dependencyFactory } from "../../../features/shared/injection";
import { IGeneralSearchHistoryService, IGeneralSearchService, SERVICES } from "../../../services";
import { ISavedGeneralSearch } from "../../../features/modules/search/search-history/saved-general-search";
import { DataView } from "primereact/dataview";
import { INeighborhoodExplorerStore } from "../../../stores/neighborhood-explorer-store";
import { ITutorialStore, STORES } from "../../../stores";
import { useNavigate } from "react-router-dom";
import { IconField } from "primereact/iconfield";
import { InputIcon } from "primereact/inputicon";
import { faL } from "@fortawesome/free-solid-svg-icons";
import { Badge } from "primereact/badge";
import { darkenHexColor, truncateString } from "../../../utils";
import GeneralSearchPageTourComponent from "./general-search-page-tour.component";



export const GeneralSearchPageComponent: React.FC = () => {
    const navigate = useNavigate();

    const [query, setQuery] = useState<string>();
    const [elements, setElements] = useState<any[]>([]);
    const [selectedElements, setSelectedElements] = useState<any[]>([]);

    const searchService = dependencyFactory.get<IGeneralSearchService>(SERVICES.IGeneralSearchService);
    const historyService = dependencyFactory.get<IGeneralSearchHistoryService>(SERVICES.IGeneralSearchHistoryService);
    const neighborhoodExplorerStore = dependencyFactory.get<INeighborhoodExplorerStore>(STORES.INeighborhoodExplorerStore);
    const tutorialStore = dependencyFactory.get<ITutorialStore>(STORES.ITutorialStore);

    const [history, setHistory] = useState<Partial<ISavedGeneralSearch>[]>([]);
    const {messageService} = useContext(MessageServiceContext);
    const [searching, setSearching] = useState<boolean>(false);


    const [runTutorial, setRunTutorial] = useState<boolean>(tutorialStore.getShowGeneralSearchTutorial());

    const helpClickedHandler = () => {
        setRunTutorial(true);
    }

    const helpTourCallback = () => {
        setRunTutorial(false);
        tutorialStore.setShowGeneralSearchTutorial(false);
        // tutorialStore.setShowCoOccurrencesSummaryTutorial(false);
    }

    useEffect(() => {
        initWidgets();
    },[])

    const initWidgets = async () => {
        setHistory(await historyService.getAllAsOptions(messageService!));
    }


    const listTemplate = (items: ISavedGeneralSearch[]) : ReactNode[] | undefined =>  {
        if (!items || items.length === 0) return undefined;


        let list = items.map((query, index) => {
            return (<div style={{padding: 5, backgroundColor: "#F8F9FA", marginBottom: 5, borderRadius: 10, border: '1px solid #DEE2E6', display: 'flex', alignItems: 'center'}}>

                    <span style={{marginLeft: 5}}>{query.query}</span>
                    
                    <div style={{marginLeft: 'auto'}}></div>
                    <Button icon="pi pi-upload" rounded text aria-label="Filter" onClick={async (e) => {

                        const loaded = await historyService.get(query.id, messageService!);
                        setElements(loaded.results);

                    }} 
                    tooltip="Load results"
                                tooltipOptions={{position: 'bottom', showDelay: 1000}}
                    />

                    <Button icon="pi pi-trash" rounded text severity="danger" aria-label="Cancel" onClick={async (e) => {
                        await historyService.delete(query.id);
                        setHistory(await historyService.getAllAsOptions(messageService!))
                        
                        }} 
                        tooltip="Remove from history"
                        tooltipOptions={{position: 'bottom', showDelay: 1000}}    
                    />


                </div>)
        });

        return [<div className="grid grid-nogutter">{list}</div>];
    };

    const runQuery = async () => {
        if (!searching && query){
            setSearching(true);
            const elements = await searchService.findEntities(query, messageService!);
            setElements(elements);
            setSelectedElements([]);
            setSearching(false);
            await historyService.create({id: "0", query: query, datetime: "", results: elements}, messageService!);
            setHistory(await historyService.getAllAsOptions(messageService!))
        }
    }

    const typeColumnTemplate = (result:any) => {
        return <Badge value={truncateString(result.type, 25)} style={{ background: darkenHexColor(result.color, -140), border: `solid 2px ${result.color}`, height: 27, color: 'black'  }}/>
        
    }

    const nameColumnTemplate = (result:any) => {
        return <span>{truncateString(result.name, 150)}</span>
    }

    return <div className="page-container-narrow">
        <PageTitle icon="fa fa-search" title="General Search" help={true} helpClickedHandler={helpClickedHandler}></PageTitle>
        
        <GeneralSearchPageTourComponent run={runTutorial} callback={helpTourCallback}></GeneralSearchPageTourComponent>

        <div className="row">
            <div className="col-3 one-health-panel" style={{height: "700px"}} id="general-search-history">
            <div className='one-health-panel-header'>
                        <i style={{marginLeft: 5, marginRight: 5}} className='fa fa-history'></i>
                        History
                    </div>

                    <div style={{height: "calc(100% - 50px)", padding: 5, overflowY: 'scroll'}}>
                                    {
                                        history.length > 0 && <DataView value={history} listTemplate={listTemplate} />
                                    }
                                    {
                                        history.length <= 0 && <CollectionPlaceholderComponent icon='pi pi-history' message='' />
                                    }
                    </div>
            </div>

            <div className="col-9" style={{height: "700px", paddingLeft: "10px"}} >
                <div  className="one-health-panel" style={{height: "100%"}} id="general-search-panel">
                    <div className="ro one-health-panel-header" style={{padding: "3px"}} id="general-search-header">
                        <div className="col-10">
                            <IconField iconPosition="left">
                                    <InputIcon className="pi pi-search"> </InputIcon>
                            <InputText 
                                style={{width: "100%"}} 
                                value={query}
                                onChange={(e) => {setQuery(e.target.value)}}
                                onKeyDown={(e) => {
                                    if(e.key == 'Enter'){
                                        runQuery();
                                    }
                                }}
                                placeholder="Search in knowledge base (e.g. disease name, plant name, compound name, InChI key, ...)"
                                >

                            </InputText>
                            </IconField>
                        </div>

                        <div className="col-2" style={{paddingLeft: "5px"}}>
                            <Button 
                                label="Search" 
                                onClick={async () => {
                                    runQuery();
                                }}
                               
                                tooltip="Search in knowledge base"
                                tooltipOptions={{position: 'bottom', showDelay: 1000}}
                            />
                            <Button 
                                icon="fa fa-compass" 
                                style={{marginLeft: 5}} 
                                onClick={() => {
                                    neighborhoodExplorerStore.nodes = neighborhoodExplorerStore.nodes.concat(selectedElements.map(x => { return {data: {id: x.id, color: x.color, label: x.name }}}));

                                    navigate('/neighborhood-explorer');
                                }}
                                tooltip="Show selection in neighborhood explorer"
                                tooltipOptions={{position: 'bottom', showDelay: 1000}}
                            />

                        </div>
                    </div>
                    <div style={{height: "calc(100% - 50px)"}}>
                    { searching && <LoadingPlaceholderComponent></LoadingPlaceholderComponent>}
                    {!searching && elements.length === 0 && <CollectionPlaceholderComponent icon="pi pi-list" message=""></CollectionPlaceholderComponent> }
                    {
                       !searching && elements.length > 0 && <DataTable 
                            scrollable 
                            scrollHeight="650px"
                            selectionMode="multiple"
                            metaKeySelection={false}
                            selection={selectedElements}
                            onSelectionChange={(e) => setSelectedElements(e.value)}
                            value={elements} 
                            tableStyle={{ minWidth: '50rem' }}>
                        <Column field="name" style={{width: "75%"}} sortable body={nameColumnTemplate} header="Name"></Column>
                        <Column field="type" sortable header="Type" body={typeColumnTemplate}></Column>
                    </DataTable>
                    }
                    </div>
                </div>
            </div>

        </div>
    </div>;
};


export default GeneralSearchPageComponent;