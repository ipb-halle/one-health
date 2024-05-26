import { ReactNode, useContext, useState } from "react";
import { StructureFilterMatchMode } from "../../../features/filters/enums/structure-filter-match-mode";
import { CollectionPlaceholderComponent, PageTitle } from "../../../components";
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
import { STORES } from "../../../stores";
import { useNavigate } from "react-router-dom";



export const GeneralSearchPageComponent: React.FC = () => {
    const navigate = useNavigate();

    const [query, setQuery] = useState<string>();
    const [elements, setElements] = useState<any[]>([]);
    const [selectedElements, setSelectedElements] = useState<any[]>([]);

    const searchService = dependencyFactory.get<IGeneralSearchService>(SERVICES.IGeneralSearchService);
    const historyService = dependencyFactory.get<IGeneralSearchHistoryService>(SERVICES.IGeneralSearchHistoryService);
    const neighborhoodExplorerStore = dependencyFactory.get<INeighborhoodExplorerStore>(STORES.INeighborhoodExplorerStore);

    const [history, setHistory] = useState<Partial<ISavedGeneralSearch>[]>([]);
    const {messageService} = useContext(MessageServiceContext);


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

    return <div className="container">
        <PageTitle icon="fa fa-search" title="General Search" help={true}></PageTitle>
        
        <div className="row">
            <div className="col-3 one-health-panel" style={{height: "700px"}}>
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
                <div  className="one-health-panel" style={{height: "100%"}}>
                    <div className="ro one-health-panel-header" style={{padding: "3px"}}>
                        <div className="col-10">
                            <InputText 
                                style={{width: "100%"}} 
                                value={query}
                                onChange={(e) => {setQuery(e.target.value)}}
                                >

                            </InputText>
                        </div>

                        <div className="col-2" style={{paddingLeft: "5px"}}>
                            <Button 
                                label="Search" 
                                onClick={async () => {
                                    if (query){

                                        console.log(query);
                                        const elements = await searchService.findEntities(query, messageService!);
                                        setElements(elements);
                                        setSelectedElements([]);
                                        await historyService.create({id: "0", query: query, datetime: "", results: elements}, messageService!);
                                        setHistory(await historyService.getAllAsOptions(messageService!))
                                    }
                                }}
                                tooltip="Search in knowledge base"
                                tooltipOptions={{position: 'bottom', showDelay: 1000}}
                            />
                            <Button 
                                icon="fa fa-compass" 
                                style={{marginLeft: 5}} 
                                onClick={() => {
                                    neighborhoodExplorerStore.setIds(selectedElements.map(x => { return {id: x.id, color: x.color, label: x.name }}));
                                    navigate('/neighborhood-explorer');
                                }}
                                tooltip="Show selection in neighborhood explorer"
                                tooltipOptions={{position: 'bottom', showDelay: 1000}}
                            />

                        </div>
                    </div>
                    <div style={{height: "calc(100% - 50px)"}}>
                    {elements.length === 0 && <CollectionPlaceholderComponent icon="pi pi-list" message=""></CollectionPlaceholderComponent> }
                    {
                        elements.length > 0 && <DataTable 
                            scrollable 
                            scrollHeight="650px"
                            selectionMode="multiple"
                            metaKeySelection={false}
                            selection={selectedElements}
                            onSelectionChange={(e) => setSelectedElements(e.value)}
                            value={elements} 
                            tableStyle={{ minWidth: '50rem' }}>
                        <Column field="name" header="Name"></Column>
                        <Column field="type" header="Type"></Column>
                    </DataTable>
                    }
                    </div>
                </div>
            </div>

        </div>
    </div>;
};


export default GeneralSearchPageComponent;