import { Panel } from 'primereact/panel';
import { Splitter, SplitterPanel } from 'primereact/splitter';

import Plot from 'react-plotly.js';
import CytoscapeComponent from 'react-cytoscapejs';
import { Divider } from 'primereact/divider';
import ElementView from './element-view.component';
import { IProperty } from '../properties';
import { DataTypes } from '../data-types/data-types';
import cytoscape, { CoseLayoutOptions } from 'cytoscape';
import { useCallback, useEffect, useRef, useState } from 'react';
import { Core } from 'cytoscape';
import { darkenHexColor, getContrastColor } from '../../utils';
import logo from '../../logo.svg';

import './explorer.component.scss';
import { dependencyFactory } from '../../injection/inversify.config';
import { IMetadataService } from '../../services/metadata-service';
import { SERVICE_TYPES } from '../../services';
import { RefObject } from 'react';
import { Toast } from 'primereact/toast';
import { IMetadataElement } from './metadata-element';
import { table } from 'console';
import CytoscapeChart from './cytoscape-chart.component';

let cola = require('cytoscape-cola');

const React = require('react');

const MemoizedChart = React.memo(CytoscapeComponent);
const MemoChart = React.memo(CytoscapeChart);


const Explorer: React.FC = () => {
    const toast : RefObject<Toast> = useRef<Toast>(null);
    const background = "#343843";
    const [element, setElement] = useState<any>({});

    cytoscape.use(cola)

    const [cytoscapeCore, setCytoscapeCore] = useState<Core>()
    const metadataService = dependencyFactory.get<IMetadataService>(SERVICE_TYPES.IMetadataService);
    const [elements, setElements] = useState<any>([]);

    const [selectedElementId, setSelectedElementId] = useState<any>({id: "", type: ""});
    const [selectedElement, setSelectedElement] = useState<IMetadataElement>({
        id: "",
        name: "",
        count: 0,
        description: "",
        properties: [],
        type: ""
    })


    useEffect(() => {
        if (cytoscapeCore){

            cytoscapeCore.on('mouseover', 'node', function (event) {
            var node = event.target;
            node.addClass('highlighted');
            console.log("in");

          });

          
      
          cytoscapeCore.on('mouseout', 'node', function (event) {
            var node = event.target;
            node.removeClass('highlighted');
            console.log("out");
          });

          cytoscapeCore.on('click', 'node', async (event) => {
            // setSelectedElementId({id: event.target.id(), type: "entity"});
            setElement(await metadataService.getEntityType(event.target.id(), toast));
            console.log("here");

          });
          
          // Add click event listener to edges
          cytoscapeCore.on('click', 'edge', function(event) {
            setSelectedElementId({id: event.target.id(), type: "link"});
          });

        }
    }, [cytoscapeCore]);

    const init = async () => {
        let graph = await metadataService.getAll(toast);
        setElements([...graph.nodes.map((x:any)=>{ return {data: x}}), ...graph.links?.map((x:any)=>{ return {data: x}})]) 
    }


    useEffect(() => {
        init();
    }, []);

    const onElementSelectedHandler = async (id: string, type: string) => {
        if(!id) return;

        console.log("repingaaaa");

        if (type === "entity"){
            setSelectedElement(await metadataService.getEntityType(id, toast));
            // element = await metadataService.getEntityType(id, toast);
        } else {
            setSelectedElement(await metadataService.getLinkType(id, toast));
            // element = await metadataService.getLinkType(id, toast);

        }
    }

    useEffect(() => {
        onElementSelectedHandler(selectedElementId.id, selectedElementId.type);
    }, [selectedElementId])
    

    useEffect(() => {
        console.log("pinga esto cambia");
    }, [element])

    
    const onNodeClickHandler = useCallback(
        async (id:any) => {setElement(await metadataService.getEntityType(id, toast));}, []
    )

    const onEdgeClickHandler = useCallback(
        async (id:any) => {setElement(await metadataService.getLinkType(id, toast));}, []
    )
    

    const styles: cytoscape.Stylesheet = {
        selector: 'node',
        style: {
            backgroundColor: (ele) => {return darkenHexColor(ele.data("color"), -140)},
            width: "75px",
            height: "75px",
            "border-width": "3px",
            "border-color": (ele) => {return ele.data("color")},
            label: (ele:any) => { return ele.data("label")},
            "text-valign": "center",
            "text-halign": "center",
            "color": (ele:any) => {return getContrastColor(darkenHexColor(ele.data("color"), -140))},
            "overlay-opacity": 0,
            // "background-opacity" : 0.5,
        }
    }

    const highlightedStyles: cytoscape.Stylesheet = {
        selector: '.highlighted',
        style: {
            // 'overlay-opacity': 0.2,
            // 'overlay-color': '#F8F9FA',
            // 'overlay-padding': '7px'
            "border-color": "#00eaff",
            "border-opacity": 0.25,
            "border-width": "13px",

        }
    }

    const edgeStyle: cytoscape.Stylesheet = {
        selector: 'edge',
        style: {
            "text-background-opacity": 1,
            "text-background-padding": "3px",
            "text-background-color": "#343843",
            "line-color": () => {return "#A5ABB6"},
            label : (ele:any) => { return ele.data("label")},
            "text-rotation": "autorotate",
            color: "white",
            "curve-style": "bezier",
            'target-arrow-shape': (ele => { return ele.data("direction") !== "UNDIRECTED" ? "triangle" : "none"})
        }
    }

    const layout: CoseLayoutOptions  = {
        name: 'cose', // Use cose layout algorithm
        animate: true, // Animate the layout
        idealEdgeLength: () => 200, // Ideal length of edges
        nodeOverlap: 20, // Amount of space to avoid between nodes
        refresh: 20, // Number of iterations between consecutive layout events
        fit: true, // Fit the graph to the viewport
        padding: 30, // Padding around the graph
        randomize: false, // Randomize node positions
        componentSpacing: 100, // Space between connected components
        nodeRepulsion: () => 400000, // Repulsion between nodes
        edgeElasticity: () => 100, // Elasticity of edges
        nestingFactor: 5, // Nesting factor
        gravity: 10, // Apply gravitational forces
        numIter: 1000, // Maximum number of iterations
        initialTemp: 1000, // Initial temperature (maximum node displacement)
        coolingFactor: 0.99, // Cooling factor (how quickly the temperature decreases)
        minTemp: 1.0, // Minimum temperature (when to stop the layout)
      }

    
      var defaults = {
        animate: true, // whether to show the layout as it's running
        refresh: 1, // number of ticks per frame; higher is faster but more jerky
        maxSimulationTime: 4000, // max length in ms to run the layout
        ungrabifyWhileSimulating: false, // so you can't drag nodes during layout
        fit: true, // on every layout reposition of nodes, fit the viewport
        padding: 30, // padding around the simulation
        boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
        nodeDimensionsIncludeLabels: false, // whether labels should be included in determining the space used by a node
      
        // layout event callbacks
        ready: function(){}, // on layoutready
        stop: function(){}, // on layoutstop
      
        // positioning options
        randomize: false, // use random node positions at beginning of layout
        avoidOverlap: true, // if true, prevents overlap of node bounding boxes
        handleDisconnected: true, // if true, avoids disconnected components from overlapping
        convergenceThreshold: 0.01, // when the alpha value (system energy) falls below this value, the layout stops
        nodeSpacing: function( ){ return 10; }, // extra spacing around nodes
        flow: undefined, // use DAG/tree flow layout if specified, e.g. { axis: 'y', minSeparation: 30 }
        alignment: undefined, // relative alignment constraints on nodes, e.g. {vertical: [[{node: node1, offset: 0}, {node: node2, offset: 5}]], horizontal: [[{node: node3}, {node: node4}], [{node: node5}, {node: node6}]]}
        gapInequalities: undefined, // list of inequality constraints for the gap between the nodes, e.g. [{"axis":"y", "left":node1, "right":node2, "gap":25}]
        centerGraph: true, // adjusts the node positions initially to center the graph (pass false if you want to start the layout from the current position)
      
        // different methods of specifying edge length
        // each can be a constant numerical value or a function like `function( edge ){ return 2; }`
        edgeLength: undefined, // sets edge length directly in simulation
        edgeSymDiffLength: undefined, // symmetric diff edge length in simulation
        edgeJaccardLength: undefined, // jaccard edge length in simulation
      
        // iterations of cola algorithm; uses default values on undefined
        unconstrIter: undefined, // unconstrained initial layout iterations
        userConstIter: undefined, // initial layout iterations with user-specified constraints
        allConstIter: undefined, // initial layout iterations with all constraints including non-overlap
      };


    return (
        <div className="explorer">
                   
                <div
                    className="row"
                    style={{ height: '600px', width: '100%', margin: '0px' }}
                >
                    <div className="col-md-9" style={{ padding: '0px' }}>
                        {/* <MemoizedChart
                            cy={(cy:any) => { setCytoscapeCore(cy) }}
                            elements={elements}
                            styleEnabled={true}
                            maxZoom={1}
                            minZoom={0.6}
                            stylesheet={[styles, edgeStyle, highlightedStyles]}
                            layout={layout}
                            // "#343843"
                            style={{ width: '100%', height: '100%', backgroundColor: background }}
                        /> */}
                        <MemoChart elements={elements} onNodeClickHandler={onNodeClickHandler} onEdgeClickHandler={onEdgeClickHandler}></MemoChart>
                        {/* <CytoscapeChartComponent></CytoscapeChartComponent> */}
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
                        <ElementView
                           element={element}
                        ></ElementView>
                    </div>
                </div>
        
        </div>
    );
};

export default Explorer;
