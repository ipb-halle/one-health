
import Plot from 'react-plotly.js';
import { SankeyData } from 'plotly.js';
import { RefObject, useContext, useEffect, useState } from 'react';
// import "./co-occurrence-search.component.scss";
import { Toolbar } from 'primereact/toolbar';
import { Button } from 'primereact/button';
import { dependencyFactory } from '../../../features/shared/injection';
import { IEntityTypeService, IOntologyService, SERVICES } from '../../../services';
import { MessageServiceContext } from '../../../features/shared/messages';
import { SelectableOption } from '../../../utils/selectable-option';
import { ICoOcurrenceQuery } from '../../../features/modules/visualization/co-ocurrence-search/co-ocurrence-query';
import TypeQueryBuilder from '../../../features/modules/visualization/co-ocurrence-search/type-query-builder';
        


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

    const [runQuery, setRunQuery] = useState<boolean>(false);

    const [typeOptions, setTypeOptions] = useState<SelectableOption[]>([]);
    const [visible, setVisible] = useState<boolean>(false);


    const [sankeyData, setSankeyData] = useState<Partial<SankeyData>>(
        {
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

        // setSankeyData({...sankeyData, node: {label: nodes}, link: {
        //     source: [0, 1, 4, 2, 1],
        //     target: [1, 4, 5, 4, 3],
        //     value: [4, 2, 3, 1, 2]
        // }})

    }

    useEffect(
        () => {
            updateData({leftTypeQuery: leftTypeQuery, rightTypeQuery: rightTypeQuery});
        }, [leftTypeQuery, rightTypeQuery]

    )

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




    const getToolbarButtons = () => {
        return <React.Fragment>
            <Button label="Apply" className="mr-2" style={{marginRight: "5px"}} onClick={() => {console.log(leftTypeQuery); setRunQuery(!runQuery);}}/>
            <Button label='Clear' severity='danger' className="mr-2" />
        </React.Fragment>
    }


    return (
        <div className='container'>

            <div className="row mb-3">
                <div className="col pt-1" style={{ maxWidth: '50px' }}>
                    <i
                        className="pi pi-share-alt"
                        style={{ fontSize: '32px' }}
                    ></i>
                </div>
                <div className="col-sm-5">
                    <h2>Co-Ocurrence Search</h2>
                </div>
            </div>


            <div className='row pb-3'>



            <Toolbar style={{width: "99.1%", padding:"5px"}} end={getToolbarButtons} />
            </div>

            <div className='row'>
            <div className='col-md-9' style={{height: "710px", border: "1px solid #DEE2E6"}}>
            <Plot
                data={[sankeyData]}
                layout={{ autosize: true }}
                useResizeHandler={true}
                style={{ width: '100%', height: '100%' }}/>
            </div>

            <div className='col-md-3'>
                <TypeQueryBuilder triggerQuery={runQuery} parentUpdate={(leftQuery:any) => { setLeftTypeQuery(leftQuery)  }}></TypeQueryBuilder>
                <TypeQueryBuilder triggerQuery={runQuery} parentUpdate={(rightQuery:any) => { setRightTypeQuery(rightQuery) }}></TypeQueryBuilder>

            </div>
            </div>

          

          
        </div>
    );
};

export default CoOcurrenceSummaryPageComponent;
