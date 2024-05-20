
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
import { IQueryHistoryGraphService } from '../../../query-history/query-history-graph/query-history-graph.service';
import { SERVICES } from '../../../../services';


const React = require('react');

const MemoChart = React.memo(CytoscapeInteractiveChartComponent);

interface GraphExplorerProps{
    graphService: any; //TODO: make an interface GetInitial, GetNodeExpansion, make option to group edges or not in the api call
};


const NeighborhoodExplorerComponent: React.FC<GraphExplorerProps> = ({graphService}) => {
    const toast : RefObject<Toast> = useRef<Toast>(null);
    const [element, setElement] = useState<any>({});
    const [queryHistory, setQueryHistory] = useState<IQueryGraph[]>([]);
    const { messageService } = useContext(MessageServiceContext);


    const queryHistoryGraphService = dependencyFactory.get<IQueryHistoryGraphService>(SERVICES.IQueryHistoryGraphService);

    const myComponentRef : RefObject<CytoscapeInteractiveChartComponent> = useRef<CytoscapeInteractiveChartComponent>(null);

    
    const [elements, setElements] = useState<any>([]);
    const [nodeQuery, setNodeQuery] = useState<string>("");

    const init = async () => {
        let graph = await graphService.getInitial(messageService!);
        const newElements = [...graph.nodes.map((x:any)=>{ return {data: x}}), ...graph.links?.map((x:any)=>{ return {data: x}})];
        setElements([...newElements]);
        setQueryHistory(queryHistoryGraphService.getAsOptions());
    };


    useEffect(() => {
        init();
    }, []);

    
    const onNodeClickHandler = useCallback(
        async (id:any) => {setElement(await graphService.getNode(id, messageService!));}, []
    )

    const onEdgeClickHandler = useCallback(
        async (id:any) => {setElement(await graphService.getEdge(id, messageService!));}, []
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

    const actionsOptions = [
        {
            label: 'Clean',
            command: () => {
               myComponentRef.current!.downloadJSON();
            }
        },
        {
            label: 'Reset View',
            command: () => {
              
            }
        },
        {
            label: 'Redo Layout',
            command: () => {
               
            }
        },
    ]


    const listTemplate = (items: IQueryGraph[]) : ReactNode[] | undefined =>  {
        if (!items || items.length === 0) return undefined;

        let list = items.map((query, index) => {
            return (<div>
                    {query.name} {query.id}
                </div>)
        });

        return [<div className="grid grid-nogutter">{list}</div>];
    };
  

    return (
        <div className="explorer">
                   
                <div
                    className="row"
                    style={{ height: '700px', width: '100%', margin: '0px' }}
                >
                    <div className="col-md-9" style={{ padding: '0px' }}>
                        <div className="row" style={{ padding: '5px'}}>

                            <div className="col-6">
                            <div className="p-inputgroup flex-1">
                                <span className="p-inputgroup-addon">
                                    <i className="pi pi-search"></i>
                                </span>
                                <InputText onChange={(e) => setNodeQuery(e.target.value)} onKeyDown={(e) => {
                                    if(e.key == 'Enter')
                                        myComponentRef.current!.findNode(nodeQuery);
                                }}/>
                                <Button icon="pi pi-angle-left" onClick={(e) => {myComponentRef.current!.previousSelectedNode()}}/>
                                <Button icon="pi pi-angle-right" onClick={(e) => {myComponentRef.current!.nextSelectedNode()}}/>

                            </div>
                            </div>
                            <div className='col-3'>
                            <SplitButton icon="pi pi-download"  model={downloadOptions} />
                            </div>
                            <div className='col-3'>
                            <div className="p-inputgroup flex-1">
                                <span className="p-inputgroup-addon">
                                    <i className="pi pi-cog"></i>
                                </span>
                                <Button icon="pi pi-file" onClick={(e) => {myComponentRef.current!.reset()}}/>
                                <Button icon="fa fa-camera-rotate" onClick={(e) => {myComponentRef.current!.resetView();}}/>
                                <Button icon="pi pi-th-large" onClick={(e) => {myComponentRef.current!.redoLayout();}}/>
                                <Button icon="pi pi-eraser" onClick={(e) => {myComponentRef.current!.clean();}}/>
                                <Button icon="pi pi-save" onClick={(e) => {
                                    queryHistoryGraphService.save({
                                        id: "2",
                                        name: "saved one",
                                        query: "jojoj"
                                    });
                                    setQueryHistory(queryHistoryGraphService.getAsOptions());

                                }}/>


                                
                            </div>
                            </div>
                        </div>
                        <div className="row" id='cjon' style={{height: "645px"}}>
                            <MemoChart 
                                ref={myComponentRef}
                                elements={elements} 
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
                                >
                            </MemoChart>
                        </div>
                    </div>

                    <div
                        className="col-md-3"
                        style={{
                            zIndex: 1,
                            padding: '0px',
                            borderLeft: '1px solid #dee2e6',
                            backgroundColor: 'white',
                        }}
                    >

                        <TabView>
                            <TabPanel header="Search">
                                <p className="m-0">
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                                    Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
                                    consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
                                    Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                                </p>
                            </TabPanel>
                            <TabPanel header="Details">
                            <ElementView
                           element={element}
                        ></ElementView>
                            </TabPanel>
                            <TabPanel header="History">
                                <div style={{width: '100%', height:'100%'}}>
                                    <DataView value={queryHistory} listTemplate={listTemplate} />
                                </div>
                            </TabPanel>
                        </TabView>
                   
                    </div>
                </div>
        
        </div>
    );
};

export default NeighborhoodExplorerComponent;
