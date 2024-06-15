
import ElementView from '../../metadata/metadata-explorer/element-view.component';
import { ReactNode, useCallback, useContext, useEffect, useRef, useState } from 'react';

import './neighborhood-explorer.component.scss';
import { RefObject } from 'react';
import { Toast } from 'primereact/toast';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { SplitButton } from 'primereact/splitbutton';
import { TabPanel, TabView } from 'primereact/tabview';
import { CytoscapeInteractiveChartComponent } from '../../../shared/cytoscape-interactive-chart';

import { DataView } from 'primereact/dataview';
import { IQueryGraph } from '../../../query-history/query-history-graph/graph-query';
import { MessageServiceContext } from '../../../shared/messages';
import { dependencyFactory } from '../../../shared/injection';
import { IEntityService, IEntityTypeService, IGeneralSearchService, IGraphVisualizationHistoryService, SERVICES } from '../../../../services';
import { ISavedGraphVisualization } from '../visualization-history/models/saved-graph-visualization';
import { ConfirmDialog, confirmDialog } from 'primereact/confirmdialog';
import { Divider } from 'primereact/divider';
import { Badge } from 'primereact/badge';
import { classNames } from 'primereact/utils';
import { darkenHexColor, truncateString } from '../../../../utils';
import { CollectionPlaceholderComponent, LoadingPlaceholderComponent } from '../../../../components';
import MolecularDrawComponent from '../../../shared/molecular-draw/molecular-draw.component';
import { INeighborhoodExplorerStore } from '../../../../stores/neighborhood-explorer-store';
import { STORES } from '../../../../stores';
import { IconField } from "primereact/iconfield";
import { InputIcon } from "primereact/inputicon";
import { IEntityType } from '../../metadata/entity-types';
import { Checkbox } from 'primereact/checkbox';
import * as XLSX from 'xlsx';


const React = require('react');

const MemoChart = React.memo(CytoscapeInteractiveChartComponent);

interface GraphExplorerProps{
    graphService: any; //TODO: make an interface GetInitial, GetNodeExpansion, make option to group edges or not in the api call
};


const NeighborhoodExplorerComponent: React.FC<GraphExplorerProps> = ({graphService}) => {
    const [element, setElement] = useState<any>({});
    const [queryHistory, setQueryHistory] = useState<Partial<ISavedGraphVisualization>[]>([]);
    const { messageService } = useContext(MessageServiceContext);
    const [selectionType, setSelectionType] = useState<"node" | "edge" | null>(null);
    const [query, setQuery] = useState<string>();
    const [queryResults, setQueryResults] = useState<any[]>([]);
    const [searching, setSearching] = useState<boolean>(false);
    const [types, setTypes] = useState<any[]>([]);
    const [tabPanelActiveIndex, setTabPanelActiveIndex] = useState<number>(0);

    let savedVisualization: ISavedGraphVisualization = {
        id: "",
        name: "Untitled",
        visualization: ""
    }
    const graphVisualizationHistoryService = dependencyFactory.get<IGraphVisualizationHistoryService>(SERVICES.IGraphVisualizationHistoryService);
    const entityTypeService = dependencyFactory.get<IEntityTypeService>(SERVICES.IEntityTypeService);
    const searchService = dependencyFactory.get<IGeneralSearchService>(SERVICES.IGeneralSearchService);


    const myComponentRef : RefObject<CytoscapeInteractiveChartComponent> = useRef<CytoscapeInteractiveChartComponent>(null);
    const entityService = dependencyFactory.get<IEntityService>(SERVICES.IEntityService);

    
    const [elements, setElements] = useState<any>([]);
    const [nodeQuery, setNodeQuery] = useState<string>("");
    const [links, setLinks] = useState<any[]>([]);
    const [selectedLink, setSelectedLink] = useState<any>(null);

    const neighborhoodExplorerStore = dependencyFactory.get<INeighborhoodExplorerStore>(STORES.INeighborhoodExplorerStore);


    const init = async () => {
        // let graph = await graphService.getInitial(messageService!);
        // const newElements = [...graph.nodes.map((x:any)=>{ return {data: x}}), ...graph.links?.map((x:any)=>{ return {data: x}})];
        // setElements([...newElements]);
        setQueryHistory(await graphVisualizationHistoryService.getAllAsOptions(messageService!));

        // const nodes = neighborhoodExplorerStore.getIds();
        // if (nodes.length > 0){
        //     const graph = {nodes:  nodes.map(x => {return {data: x}}), edges: []};
        //     myComponentRef.current!.setElements(JSON.stringify(graph));
        // } else {
        //     const viz = await graphVisualizationHistoryService.get("0", messageService!);
        //     myComponentRef.current!.setElements(viz.visualization);
        // }

        // console.log(neighborhoodExplorerStore.elements);
        // if (neighborhoodExplorerStore.elements?.nodes){
        //     console.log("here");
        //     console.log(neighborhoodExplorerStore.elements.nodes);
        //     myComponentRef.current!.setElements(JSON.stringify(neighborhoodExplorerStore.elements))
        // }

        const entityTypes = await entityTypeService.getAll(messageService!);
        setTypes(entityTypes.map(x => {return {name: x.name, id: x.id, color: x.color}}));
    };




    useEffect(() => {
        init();

    }, []);
    
    const onNodeClickHandler = useCallback(
        async (id:any) => {
            setElement(await graphService.getNode(id, messageService!)); 
            setSelectionType('node');
            setTabPanelActiveIndex(1);
        }, []
    )

    const onEdgeClickHandler = useCallback(
        async (edge:any) => {
            const edges = await graphService.getLinksBetween(edge.data('source'),edge.data('target'),edge.data('label'), messageService!);
            setLinks(edges);
            setSelectedLink(edge);
            setSelectionType('edge');
            setTabPanelActiveIndex(1);

        }, []
    )

    const onNodeExpandHandler = useCallback(
        (ele:any) => {console.log(ele.id())}, [] 
    )

    const onNodeHideHandler = useCallback(
        (ele:any) => { 
            setElements(
                (prevState: any) => {
                    return [...prevState.filter( (x: any) => !(
                        x.data.id === ele.id()
                        || x.data.source === ele.id()
                        || x.data.target === ele.id())

                       )];
                }
            );}, []
    )

    const downloadOptions = [
        {
            label: 'XLXS',
            command: async () => {

                const results = await entityService.getGraphReferences(myComponentRef.current!.getNodes(), messageService!);


                const ws = XLSX.utils.json_to_sheet(results);
                const wb = XLSX.utils.book_new();
                XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
                XLSX.writeFile(wb, 'co-ocurrences.xlsx');

            }
        },
        {
            label: 'Cytoscape JSON',
            command: () => {
               myComponentRef.current!.downloadJSON();
            }
        },
        {
            label: 'PNG',
            command: () => {
                myComponentRef.current!.downloadPNG();
            }
        },

    ]

    const nodePropertiesTemplate = (items: any[]) : ReactNode[] | undefined =>  {
        if (!items || items.length === 0) return undefined;

        let list = items.sort((a,b) => a.position - b.position ).map((query, index) => {
            return (<div style={{padding: 5, backgroundColor: "#F8F9FA", marginBottom: 5, borderRadius: 10, border: '1px solid #DEE2E6', overflowX: 'scroll'} }>

                   

                    <span style={{marginLeft: 5}}><b>{query.name}</b>: {query.value}</span>
                  
                </div>)
        });



        return [
          <>
            <b style={{paddingLeft: "3px"}}>Properties:</b>
            {list}
          </>
            ];
    };

    const nodeReferencesTemplate = (items: any[]) : ReactNode[] | undefined =>  {
        if (!items || items.length === 0) return undefined;

        let list = items.map((query, index) => {
            return (<div style={{padding: 5, backgroundColor: "#F8F9FA", marginBottom: 5, borderRadius: 10, border: '1px solid #DEE2E6', overflowX: 'scroll'} }>

                   
                    <div>

                    <b style={{marginLeft: 5}}>Source:</b>  <span style={{marginLeft: 5}}>{query.source}</span>
                    </div>
                    <div>

                    <b style={{marginLeft: 5}}>Link:</b>
                    {
                        !query.sourceUrl && <span style={{marginLeft: 5}}>Not Available</span>
                    }
                    {

                        query.sourceUrl && <a style={{textDecoration: 'none', fontSize: 14, paddingLeft: 5}} className='pi pi-external-link' href={query.sourceUrl} target="_blank"></a>
                    }
                    </div>
                  
                </div>)
        });



        return [
          <>
            <b style={{paddingLeft: "3px"}}>References:</b>
            {list}
          </>
            ];
    };

    const linkListTemplate = (items: any[]) : ReactNode[] | undefined =>  {
        if (!items || items.length === 0) return undefined;

        let list = items.map((query, index) => {
            return (<div style={{padding: 5, backgroundColor: "#F8F9FA", marginBottom: 5, borderRadius: 10, border: '1px solid #DEE2E6'}}>

                    <div>

                    <b style={{marginLeft: 5}}>Source:</b>  <span style={{marginLeft: 5}}>{query.sourceName}</span>
                    </div>
                    <div>

                    <b style={{marginLeft: 5}}>Link:</b>
                    {
                        !query.sourceUrl && <span style={{marginLeft: 5}}>Not Available</span>
                    }
                    {

                        query.sourceUrl && <a style={{textDecoration: 'none', fontSize: 14, paddingLeft: 5}} className='pi pi-external-link' href={query.sourceUrl} target="_blank"></a>
                    }
                    </div>
                  
                </div>)
        });

        return [
            <div className="grid grid-nogutter">
                <div style={{ overflow: 'scroll'}}>

                <Badge value={truncateString(selectedLink.source().data('label'), 25)} style={{ background: darkenHexColor(selectedLink.source().data('color'), -140), border: `solid 2px ${selectedLink.source().data('color')}`, height: 27, color: 'black', marginRight: 3, marginBottom: 3 }}/>
                <Badge value={truncateString(selectedLink.data('label'), 25)} style={{background: '#E9ECEF', border: 'solid 2px #CED4DA', height: 27, color: 'black' }}/>


                <Badge value={truncateString(selectedLink.target().data('label'),25)} style={{ background: darkenHexColor(selectedLink.target().data('color'), -140), border: `solid 2px ${selectedLink.target().data('color')}`, height: 27, color: 'black'  }}/>

            {/* {selectedLink.source().data('label')} - {selectedLink.data('label')} - {selectedLink.target().data('label')} */}
                </div>
                <Divider></Divider>
            {list}
            </div>];
    };


    const queryResultsTemplate = (items: any[]) : ReactNode[] | undefined =>  {
        if (!items || items.length === 0) return undefined;


        let list = items.map((query, index) => {
            return (<div style={{padding: 5, backgroundColor: "#F8F9FA", marginBottom: 5, borderRadius: 10, border: '1px solid #DEE2E6', display: 'flex', alignItems: 'center'}}>

                    <b style={{marginLeft: 5, marginRight: 10}}>{truncateString(query.name,25)}</b>
                    <Badge value={truncateString(query.type, 25)} style={{ background: darkenHexColor(query.color, -140), border: `solid 2px ${query.color}`, height: 27, color: 'black'  }}/>

                    
                    <div style={{marginLeft: 'auto'}}></div>
                    <Button icon="pi pi-plus" severity='success'  rounded text aria-label="Filter" onClick={async (e) => {
                        myComponentRef.current!.addElement({data:{id:query.id, label:query.name, color: query.color}});
                       

                    }} 
                    tooltip="Add to graph"
                    tooltipOptions={{position: 'bottom', showDelay: 1000}}
                    />

                    {/* <Button icon="pi pi-trash" rounded text severity="danger" aria-label="Cancel" onClick={async (e) => {
                        await graphVisualizationHistoryService.delete(query.id);
                        setQueryHistory(await graphVisualizationHistoryService.getAllAsOptions(messageService!))
                        
                        }} /> */}
                </div>)
        });

        return [<div className="grid grid-nogutter">{list}</div>];
    };

    const listTemplate = (items: ISavedGraphVisualization[]) : ReactNode[] | undefined =>  {
        if (!items || items.length === 0) return undefined;


        let list = items.map((query, index) => {
            return (<div style={{padding: 5, backgroundColor: "#F8F9FA", marginBottom: 5, borderRadius: 10, border: '1px solid #DEE2E6', display: 'flex', alignItems: 'center'}}>

                    <span style={{marginLeft: 5}}>{query.name}</span>
                    
                    <div style={{marginLeft: 'auto'}}></div>
                    <Button icon="pi pi-upload" rounded text aria-label="Filter" onClick={async (e) => {

                        const loaded = await graphVisualizationHistoryService.get(query.id, messageService!);
                        myComponentRef.current!.setElements(loaded.visualization);

                    }} 
                    tooltip="Load graph"
                    tooltipOptions={{position: 'bottom', showDelay: 1000}}
                    />

                    <Button icon="pi pi-trash" rounded text severity="danger" aria-label="Cancel" onClick={async (e) => {
                        await graphVisualizationHistoryService.delete(query.id);
                        setQueryHistory(await graphVisualizationHistoryService.getAllAsOptions(messageService!))
                        
                        }}
                        tooltip="Delete graph"
                        tooltipOptions={{position: 'bottom', showDelay: 1000}}
                        />
                </div>)
        });

        return [<div className="grid grid-nogutter">{list}</div>];
    };

    const typeListTemplate = (items: IEntityType[]) : ReactNode[] | undefined =>  {
        if (!items || items.length === 0) return undefined;


        let list = items.map((query, index) => {
            return (
            
                <div style={{padding: 5, marginBottom: 5, display: 'flex', alignItems: 'center', gap:"10px"}}>

                    <Checkbox checked></Checkbox>
                    <Badge value={query.name} style={{ background: darkenHexColor(query.color, -140), border: `solid 2px ${query.color}`, height: 27, color: 'black'  }}/>

                </div>)
        });

        return [<div className="grid grid-nogutter">{list}</div>];
    };

    const accept =  async() => {
        // const elements = myComponentRef.current!.getElements();
        // console.log(savedVisualization);
        // const item : ISavedGraphVisualization = {...savedVisualization, visualization: elements};
        await graphVisualizationHistoryService.create(savedVisualization, messageService!);
        setQueryHistory(await graphVisualizationHistoryService.getAllAsOptions(messageService!));
    };

    const reject = () => {
    };

    const confirmGraphVisualizationSave = () => {
        confirmDialog({
            header: 'Save graph visualization',
            message: (<div>

                <div className="flex flex-column align-items-center w-full border-bottom-1 surface-border" style={{marginBottom: 5}}>
                    <i className="pi pi-exclamation-circle text-6xl text-primary-500" style={{marginRight: 5}}></i>
                    <span>Please introduce a name for the visualization.</span>
                </div>
                    <InputText onChange={(e) => {savedVisualization.name = e.target.value}} style={{width:"100%"}}/>
            </div>

            ),
            acceptLabel: "Save",
            rejectLabel: "Cancel",
            accept,
            reject,
        });
    };
  

    return (
        <div >
                <ConfirmDialog />
                <div className='row'>
                    <div className='col-2' style={{border: '1px solid #dee2e6', padding: 0}} id='neighborhood-explorer-history'>
                        <div className='neighborhood-explorer-panel-header'>
                        <i style={{marginLeft: 5, marginRight: 5}} className='fa fa-history'></i>
                        History
                        </div>
                    <div style={{width: '100%', height:'750px', overflowY: 'scroll', padding: 3}}>

                                    {
                                        queryHistory.length > 0 && <DataView value={queryHistory} listTemplate={listTemplate} />
                                    }
                                    {
                                        queryHistory.length <= 0 && <CollectionPlaceholderComponent icon='pi pi-history' message='' />
                                    }
                                    
                    
                    </div>
                    </div>
                    <div className='col-10'>
                    <div className="row neighborhood-explorer" id='neighborhood-explorer'>
                    <div className="col-md-9" style={{ padding: '0px', height: '100%' }}>
                        <div className="neighborhood-explorer-toolbar" id="neighborhood-explorer-toolbar">

                            <div className='col-6'>

                                <div className="p-inputgroup flex-1">
                                    <span className="p-inputgroup-addon">
                                        <i className="pi pi-search"></i>
                                    </span>
                                    <InputText placeholder='Search for node...' onChange={(e) => setNodeQuery(e.target.value)} onKeyDown={(e) => {
                                        if(e.key == 'Enter')
                                            myComponentRef.current!.findNode(nodeQuery);

                                    }}
                                    tooltip="Find string in node name"
                                    tooltipOptions={{position: 'bottom', showDelay: 1000}}
                                    />
                                    <Button 
                                        icon="pi pi-angle-left" 
                                        onClick={(e) => {myComponentRef.current!.previousSelectedNode()}}
                                        tooltip="Previous match"
                                        tooltipOptions={{position: 'bottom', showDelay: 1000}}
                                        
                                        />
                                    <Button 
                                        icon="pi pi-angle-right" 
                                        onClick={(e) => {myComponentRef.current!.nextSelectedNode()}}
                                        tooltip="Next match"
                                        tooltipOptions={{position: 'bottom', showDelay: 1000}}
                                        />

                                </div>
                            </div >
                         

                          
                            <div style={{marginLeft: 'auto', marginRight: 5}}>

                                <div className="p-inputgroup flex-1" >
                                    <span className="p-inputgroup-addon">
                                        <i className="pi pi-cog"></i>
                                    </span>
                                    <Button 
                                        icon="pi pi-file" 
                                        onClick={(e) => {myComponentRef.current!.reset()}}
                                        tooltip="New graph"
                                        tooltipOptions={{position: 'bottom', showDelay: 1000}}
                                    />
                                    <Button 
                                        icon="fa fa-camera-rotate" 
                                        onClick={(e) => {myComponentRef.current!.resetView();}}
                                        tooltip="Fit graph in camera"
                                        tooltipOptions={{position: 'bottom', showDelay: 1000}}    
                                    />
                                    <Button
                                         icon="pi pi-th-large" 
                                         onClick={(e) => {myComponentRef.current!.redoLayout();}}
                                         tooltip="Redo layout"
                                         tooltipOptions={{position: 'bottom', showDelay: 1000}}
                                    
                                    />
                                    <Button 
                                        icon="pi pi-eraser" 
                                        onClick={(e) => {myComponentRef.current!.clean();}}
                                        tooltip="Clean unlocked nodes"
                                        tooltipOptions={{position: 'bottom', showDelay: 1000}}
                                    
                                    />
                                    <Button 
                                        icon="pi pi-save" 
                                        onClick={ async (e) => {
                                            const elements = myComponentRef.current!.getElements();



                                            savedVisualization = {
                                                id: "",
                                                name: "Untitled",
                                                visualization: elements,
                                            };
                                            confirmGraphVisualizationSave();
                                        }}
                                        tooltip="Save graph"
                                        tooltipOptions={{position: 'bottom', showDelay: 1000}}
                                    />


                                
                                </div>
                            </div>

                            <SplitButton 
                                icon="pi pi-download" 
                                model={downloadOptions} 
                                tooltip="Download graph"
                                tooltipOptions={{position: 'bottom', showDelay: 1000}}
                                onClick={async () => {

                                    const results = await entityService.getGraphReferences(myComponentRef.current!.getNodes(), messageService!);
                    
                    
                                    const ws = XLSX.utils.json_to_sheet(results);
                                    const wb = XLSX.utils.book_new();
                                    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
                                    XLSX.writeFile(wb, 'co-ocurrences.xlsx');
                    
                                }}
                            
                            />
                        </div>
                        <div className="row" style={{height: "calc(100% - 50px)", padding: 2}}>
                            <div style={{height: "100%"}}>

                            <MemoChart 
                                key="cytoscapeChart"
                                ref={myComponentRef}
                                elements={[]} 
                                contextMenu={true}
                                onNodeClickHandler={onNodeClickHandler} 
                                onEdgeClickHandler={onEdgeClickHandler}
                                onNodeExpandHandler={onNodeExpandHandler}
                                onNodeHideHandler={onNodeHideHandler} 
                                backgroundColor="white"
                                edgeLineColor="black"
                                edgeLabelColor="black"   
                                graphService={graphService}  
                                messageService={messageService!}   
                                store={neighborhoodExplorerStore}                   
                                >
                            </MemoChart>
                            </div>
                        </div>
                    </div>

                    <div
                        className="col-md-3"
                        style={{
                            padding: '0px',
                            borderLeft: '1px solid #dee2e6',
                            backgroundColor: 'white',
                        }}
                    >


                        <TabView activeIndex={tabPanelActiveIndex} onTabChange={(e) => setTabPanelActiveIndex(e.index)}>
                            <TabPanel header="Legend">
                                <DataView value={types} listTemplate={typeListTemplate}>

                                </DataView>
                            </TabPanel>

                            <TabPanel header="Details">
                                <div id='neighborhood-explorer-details'>

                                {
                                    !selectionType && <div style={{width: '100%', height:'710px',}}><CollectionPlaceholderComponent icon='pi pi-list' message=''/> </div>
                                }
                                {selectionType === "node" &&  <div style={{width: '100%', height:'710px', overflowY: 'scroll'}}>
                                        <div className="grid grid-nogutter">
                                            <div >
                                            {/* <Badge value={element.type}/> */}

                                            <Badge value={element.type} style={{ background: darkenHexColor(element.color, -140), border: `solid 2px ${element.color}`, height: 27, color: 'black', marginRight: 3, marginBottom: 3 }}/>
                                            </div>
                                            <Divider></Divider>
                                            <MolecularDrawComponent element={element}></MolecularDrawComponent>
                                            <canvas id="mol_structure" width="500" height="500" style={{display: 'none'}}></canvas>
                                            <DataView value={element.properties} listTemplate={nodePropertiesTemplate} />
                                            <DataView value={element.references} listTemplate={nodeReferencesTemplate} />
                                            </div>
                                    </div>
                                }
                                {selectionType === "edge" && 
                                <div style={{width: '100%', height:'710px', overflowY: 'scroll'}}>
                                    <DataView value={links} listTemplate={linkListTemplate} />
                                </div>}
                                
                                </div>
                            </TabPanel>

                            <TabPanel header="DB Search">

                                <IconField iconPosition="left">
                                    <InputIcon className="pi pi-search"> </InputIcon>
                                    
                                <InputText 
                                    
                                    value={query}
                                    onChange={(e) => {setQuery(e.target.value);}}
                                    onKeyDown={async (e) => {
                                        if(e.key == 'Enter' && !searching){
                                            setSearching(true);
                                            setQueryResults(await searchService.findEntities(query!, messageService!));
                                            setSearching(false);
                                        }
                                            
                                    }}
                                    placeholder='Search in knowledge base...'
                                    style={{width: "100%"}}
                                    >
                                        
                                    </InputText>
                                </IconField>

                                    <Divider></Divider>
                                    <div style={{width: '100%', height:'640px', overflowY: 'scroll'}}>
                                        {
                                            searching && <LoadingPlaceholderComponent></LoadingPlaceholderComponent>
                                        }
                                    {
                                        !searching && queryResults.length <= 0 && <CollectionPlaceholderComponent icon='pi pi-list' message=''></CollectionPlaceholderComponent>
                                    }
                                    {
                                      !searching && queryResults.length > 0 && <DataView value={queryResults} listTemplate={queryResultsTemplate} />
                                    }
                                    </div>


                            </TabPanel>

                         
                            
                        </TabView>

                   
                    </div>
                </div>
                    </div>

                </div>
             
            
        
        </div>
    );
};

export default NeighborhoodExplorerComponent;
