
import Plot from 'react-plotly.js';
import { SankeyData } from 'plotly.js';
import { ReactNode, RefObject, useContext, useEffect, useRef, useState } from 'react';
// import "./co-occurrence-search.component.scss";
import { Toolbar } from 'primereact/toolbar';
import { Button } from 'primereact/button';
import { dependencyFactory } from '../../../features/shared/injection';
import { IEntityTypeService, IOntologyService, SERVICES } from '../../../services';
import { MessageServiceContext } from '../../../features/shared/messages';
import { SelectableOption } from '../../../utils/selectable-option';
import { ICoOcurrenceQuery } from '../../../features/modules/visualization/co-ocurrence-search/co-ocurrence-query';
import TypeQueryBuilder from '../../../features/modules/visualization/co-ocurrence-search/type-query-builder';
import { PageTitle } from '../../../components';
import { ConfirmDialog, confirmDialog } from 'primereact/confirmdialog';
import { SplitButton } from 'primereact/splitbutton';
import "./co-ocurrence-summary-page.component.scss";
import { TabPanel, TabView } from 'primereact/tabview';
import { ISavedCoOcurrenceVisualization } from '../../../features/modules/visualization';
import { InputText } from 'primereact/inputtext';
import { ICoOcurrenceVisualizationHistoryService } from '../../../services/modules/visualization/co-ocurrence-visualization-history-service';
import { DataView } from 'primereact/dataview';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { ITypeQuery } from '../../../features/modules/visualization/co-ocurrence-search/type-query';
import { link } from 'fs';
        


const React = require('react');

var data: SankeyData = {
    node: {
        label: ['0', '1', '2', '3', '4', '5'],
    },
    link: {
        source: [0, 1, 4, 2, 1,0],
        target: [1, 4, 5, 4, 3,1],
        value: [4, 2, 3, 1, 2,10],
    },
    type: 'sankey',
    name: 'myplot',
    orientation: 'h',
    visible: true,
    legend: 'jdflkjd',
    legendrank: 1,
    legendgrouptitle: {},
    legendwidth: 123,
    ids: [],
    hoverinfo: 'kdjfld',
    meta: 123,
    customdata: [],
    domain: {},
    textfont: {},
    selectpoints: 13,
    arrangement: 'freeform',
    hoverlabel: {},
    valueformat: 'dfdf',
    valuesuffix: 'dfdf',
    uirevision: 23,
};

const CoOcurrenceSummaryPageComponent: React.FC = () => {
    const ontologyService = dependencyFactory.get<IOntologyService>(SERVICES.IOntologyService);
    const entityService = dependencyFactory.get<IEntityTypeService>(SERVICES.IEntityTypeService);
    const {messageService} = useContext(MessageServiceContext);
    const coOcurrenceVisualizationHistoryService = dependencyFactory.get<ICoOcurrenceVisualizationHistoryService>(SERVICES.ICoOcurrenceVisualizationHistoryService); 

    const [activeIndex, setActiveIndex] = useState<number>(0);
    const [runQuery, setRunQuery] = useState<boolean>(false);

    const [queryHistory, setQueryHistory] = useState<Partial<ISavedCoOcurrenceVisualization>[]>([]);
    const [typeOptions, setTypeOptions] = useState<SelectableOption[]>([]);
    const [visible, setVisible] = useState<boolean>(false);

    const leftQueryBuilder : RefObject<TypeQueryBuilder> = useRef<TypeQueryBuilder>(null);
    const rightQueryBuilder : RefObject<TypeQueryBuilder> = useRef<TypeQueryBuilder>(null);

    const [links, setLinks] = useState<any>([]);


    let savedVisualization: any = {
        id: "",
        name: "Untitled",
        visualization: "",
        query: ""
    };

    const emptySankey : Partial<SankeyData> =    {
        node: {
            label: [],
        },
        link: {
            source: [],
            target: [],
            value: [],
        },
        type: 'sankey',
        name: 'myplot',
        orientation: 'h',
        visible: true,
        // legend: '',
        // legendrank: 1,
        // legendgrouptitle: {},
        // legendwidth: 123,
        // ids: [],
        // hoverinfo: 'hello',
        // meta: 123,
        // customdata: [],
        // domain: {},
        // textfont: {},
        // selectpoints: 13,
        // arrangement: 'freeform',
        // hoverlabel: {},
        // valueformat: '',
        // valuesuffix: '',
        // uirevision: 23,
    }
    const [sankeyData, setSankeyData] = useState<Partial<SankeyData>>(
     emptySankey
    )





    const [query, setQuery] = useState<ICoOcurrenceQuery>( {
        leftTypeQuery : {
            type: "Plant",
            groupBy: "Family"
        },
        rightTypeQuery : {
            type: "Location",
            groupBy: "Name"
        }
    });


    const [leftTypeQuery, setLeftTypeQuery] = useState<any>({});
    const [rightTypeQuery, setRightTypeQuery] = useState<any>({});

    const init = async () => {
        updateData({});

        setTypeOptions(
            await entityService.getAllEntityTypesAsOptions(messageService!)
        );

        setQueryHistory(await coOcurrenceVisualizationHistoryService.getAllAsOptions(messageService!));

        // setSankeyData({...sankeyData, node: {label: nodes}, link: {
        //     source: [0, 1, 4, 2, 1],
        //     target: [1, 4, 5, 4, 3],
        //     value: [4, 2, 3, 1, 2]
        // }})

    }


    const updateData = async (query:any) => {
        console.log("repingaaaaa");
        console.log(query);

        if (!(query?.leftTypeQuery?.type) || !(query?.rightTypeQuery?.type)){
            console.log("aquiii");
            return;
        }

        
        console.log("cojoneee siiiii");

        var graph = await ontologyService.getCoOcurrences(query, messageService!);

        if (!graph.nodes || !graph.links)
            return;

        console.log("here");
        console.log(graph);

        var nodesMap = new Map();
        var nodes: string[] = [];
        var colors: string[] = [];

        graph.nodes.forEach((x:any,i:number) => {
            nodesMap.set(x.label, i);
            nodes.push(x.label);
            colors.push(x.color);
        });

        var sourcex: number[] = [];
        var targetx: number[] = [];
        var valuex: number[] = [];
        var labelx: string[] = [];

        console.log(nodesMap);

        graph.links.forEach((x:any) => {
            sourcex.push(nodesMap.get(x.source));
            targetx.push(nodesMap.get(x.target));
            valuex.push(x.value);
            labelx.push(`${x.source} - ${x.label} - ${x.target}`);
        });

        // labelx.push(`${x.source} - ${x.label} - ${x.target}`);


        console.log(sourcex);

        setLeftTypeQuery(query.leftTypeQuery);
        setRightTypeQuery(query.rightTypeQuery);

        setSankeyData({...sankeyData, node: {label: nodes, color: colors}, link: {
            source: sourcex,
            target: targetx,
            value: valuex,
            label: labelx
        }})

    }

    useEffect(
        () => {
            init();
        }, []
    )

    const downloadOptions = [
        {
            label: 'JSON',
            command: () => {
            }
        },
        {
            label: 'PNG',
            command: () => {

            }
        },

    ]

    const accept =  async() => {

        savedVisualization.visualization = JSON.stringify(sankeyData);
        savedVisualization.query = {
            leftTypeQuery: {...leftQueryBuilder.current!.getQuery()}, 
            rightTypeQuery: {...rightQueryBuilder.current!.getQuery()}
        };
      
        await coOcurrenceVisualizationHistoryService.create(savedVisualization, messageService!);
        setQueryHistory(await coOcurrenceVisualizationHistoryService.getAllAsOptions(messageService!));
    };

    const reject = () => {
        console.log("rejected");
    };

    const listTemplate = (items: ISavedCoOcurrenceVisualization[]) : ReactNode[] | undefined =>  {
        if (!items || items.length === 0) return undefined;

        let list = items.map((query, index) => {
            return (<div style={{padding: 5, backgroundColor: "#F8F9FA", marginBottom: 5, borderRadius: 10, border: '1px solid #DEE2E6', display: 'flex', alignItems: 'center'}}>

                    <span style={{marginLeft: 5}}>{query.name}</span>
                    
                    <div style={{marginLeft: 'auto'}}></div>
                    <Button icon="pi pi-upload" rounded text aria-label="Filter" onClick={async (e) => {

                        const loaded = await coOcurrenceVisualizationHistoryService.get(query.id, messageService!);
                        const newData = JSON.parse(loaded.visualization);
                        setActiveIndex(0);
                        setSankeyData(newData)

                        leftQueryBuilder.current!.loadQuery(loaded.query.leftTypeQuery);
                        rightQueryBuilder.current!.loadQuery(loaded.query.rightTypeQuery);

                    }} />

                    <Button icon="pi pi-trash" rounded text severity="danger" aria-label="Cancel" onClick={async (e) => {
                        await coOcurrenceVisualizationHistoryService.delete(query.id);
                        setQueryHistory(await coOcurrenceVisualizationHistoryService.getAllAsOptions(messageService!))
                        
                        }} />
                </div>)
        });

        return [<div className="grid grid-nogutter">{list}</div>];
    };

    const confirmCoOcurrenceVisualizationSave = () => {
        confirmDialog({
            header: 'Save co-ocurrences visualization',
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
        <div className='page-container'>

            <PageTitle title='Co-Ocurrence Summary' icon='fa fa-circle-nodes' help={true}></PageTitle>

            
            <ConfirmDialog/>



            <div className='row g-3 coocurrences-summary'>

                {/* history bar */}
                <div className='col-2 coocurrences-summary-panel'>

                    <div className='coocurrences-summary-panel-header'>
                        <i style={{marginLeft: 5, marginRight: 5}} className='fa fa-history'></i>
                        History
                    </div>

                    <div style={{width: '100%', height:'970px', padding: 5, overflowY: 'scroll'}}>
                        <DataView value={queryHistory} listTemplate={listTemplate} />
                    </div>

                </div>


                <div className='col-10' style={{}}>
                    <div className='coocurrences-summary-panel'>

                        <div className='coocurrences-summary-panel-header'>
                            <div className="p-inputgroup flex-1" >
                                <span className="p-inputgroup-addon">
                                    <i className="pi pi-cog"></i>
                                </span>
                                <Button icon="pi pi-file" onClick={() => {
                                    setSankeyData(emptySankey);
                                    leftQueryBuilder.current!.reset();
                                    rightQueryBuilder.current!.reset();

                                }}/>
                                <Button icon="pi pi-filter" onClick={() => {
                                    updateData(
                                        {
                                            leftTypeQuery: {...leftQueryBuilder.current!.getQuery()},
                                            rightTypeQuery: {...rightQueryBuilder.current!.getQuery()}
                                        })
                                    }}/>
                                <Button icon="pi pi-save" onClick={() => { confirmCoOcurrenceVisualizationSave()}}/>
                            </div>
                            
                            <SplitButton icon="pi pi-download" model={downloadOptions} />
                        </div>

                        <div className='row' style={{height: 'calc(100% - 349px)'}}>
                            
                            <div style={{height: "100%", width: "77%",  paddingLeft: 15, paddingRight: 1}}>
                                <Plot
                                    data={[sankeyData]}
                                    layout={{ autosize: true }}
                                    useResizeHandler={true}
                                    onClick={async (event:any) => 
                                        {
                                            console.log(event.points[0].source.label);
                                            console.log(event.points[0].target.label);
                                            const newLeftQuery: ITypeQuery = {...leftTypeQuery};
                                            const newRightQuery: ITypeQuery = {...rightTypeQuery};
                                            
                                            if (newLeftQuery.filters){
                                                newLeftQuery.filters = [...newLeftQuery.filters!, {property: newLeftQuery.groupBy!, value: event.points[0].source.label}];
                                            }
                                            else newLeftQuery.filters = [{property: newLeftQuery.groupBy!, value: event.points[0].source.label}];
                                           
                                            if (newRightQuery.filters){
                                                newRightQuery.filters = [...newRightQuery.filters!, {property: newRightQuery.groupBy!, value: event.points[0].target.label}];
                                            }
                                            else newRightQuery.filters = [{property: newRightQuery.groupBy!, value: event.points[0].target.label}];
                                            
                                            console.log(newLeftQuery);
                                            console.log(newRightQuery);
                                            setLinks(await ontologyService.getCoOccurrencesDetails({leftTypeQuery: newLeftQuery, rightTypeQuery: newRightQuery}, messageService!));
                                        
                                        }}
                                    style={{ width: '100%', height: '100%' }}/>
                            </div>

                            <div style={{padding: 4, borderLeft: '1px solid #dee2e6', width: "23%", height: "100%", boxSizing: 'border-box'}}>
                                    <TypeQueryBuilder height={332} width={'calc(100% - 12px)'}  ref={leftQueryBuilder} triggerQuery={runQuery} parentUpdate={(leftQuery:any) => { setLeftTypeQuery(leftQuery)  }} messageService={messageService!}></TypeQueryBuilder>
                                    <TypeQueryBuilder height={332} width={'calc(100% - 12px)'} ref={rightQueryBuilder} triggerQuery={runQuery} parentUpdate={(rightQuery:any) => { setRightTypeQuery(rightQuery) }} messageService={messageService!}></TypeQueryBuilder>
                            </div>

                        </div>

                        <div className='row' style={{height: '300px', paddingLeft: "12px", paddingRight: "12px"}}>
                            <div style={{borderTop: '1px solid #dee2e6', padding: 0}}>
                                <DataTable scrollable scrollHeight="299px" value={links} tableStyle={{ minWidth: '50rem' }}>
                                    <Column field="leftEntity" header="Left Entity"></Column>
                                    <Column field="type" header="Relationship"></Column>
                                    <Column field="rightEntity" header="Right Entity"></Column>
                                    <Column field="sourceName" header="Source Name"></Column>
                                    <Column field="sourceUrl" header="Source URL"></Column>
                                </DataTable>
                            </div>
                        </div>
            
                    </div>
                </div>



            </div>

            

          
        </div>
    );
};

export default CoOcurrenceSummaryPageComponent;
