import { dependencyFactory } from "../../injection/inversify.config";
import { ILinkTypeService } from "../../services";
import { SERVICE_TYPES } from "../../services";
import { RefObject, useContext, useState } from "react";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import { Column } from "primereact/column";
import LazyLoadGrid from "../../utils/grid/grid.component";
import CytoscapeComponent from "react-cytoscapejs";
import { useEffect } from "react";
import cytoscape, { CoseLayoutOptions } from "cytoscape";
import { darkenHexColor } from "../../utils";
import { getContrastColor } from "../../utils";
import buttonSvg from "./svg-path.svg";
import cxtmenu from 'cytoscape-cxtmenu';
import Cytoscape from 'cytoscape';
import { faL } from "@fortawesome/free-solid-svg-icons";
import { GraphService } from "../../services/interfaces/graph-service";
import { MessageServiceContext } from "../../messages";
import { group } from "console";

Cytoscape.use(cxtmenu);



const React = require('react');



interface CytoscapeChartProps{
    elements:any;
    contextMenu: boolean;
    onNodeClickHandler:any;
    onEdgeClickHandler:any;
    onNodeExpandHandler: any;
    onNodeHideHandler: any;
    backgroundColor: any;
    edgeLineColor: any;
    edgeLabelColor: any;
    graphService: GraphService;
};

const HIGHLIGHT = "__highlight";

const CytoscapeChart: React.FC<CytoscapeChartProps> = 
    ({elements, contextMenu=false, onNodeClickHandler, onEdgeClickHandler, onNodeExpandHandler, onNodeHideHandler, backgroundColor="#343843", edgeLineColor="#A5ABB6", edgeLabelColor="white", graphService}) => {
    const [cytoscapeCore, setCytoscapeCore] = useState<any>();
    let menu = {};
    const { messageService } = useContext(MessageServiceContext);


    useEffect(() => {
        if (cytoscapeCore){

            // Configuration for highlighting the nodes when the user moves the mouse over a node
            cytoscapeCore.on('mouseover', 'node', function (event:any) {
                var node = event.target;

                if (node.id() === HIGHLIGHT)
                    return;

                var highlight = cytoscapeCore.add({ 
                    position: node.position(), 
                    data: { 
                        id: HIGHLIGHT, 
                        label: "", 
                    } 
                });

                highlight.addClass("highlight");  
            });

            cytoscapeCore.on('mouseout', 'node', function (event:any) {
                var node = event.target;

                if (node.id() === HIGHLIGHT)
                    return;

                cytoscapeCore.remove(`node[id="${HIGHLIGHT}"]`);
            });

            // Configuration for the context menu that appears when the user holds the click on a node
            if (contextMenu) {
                cytoscapeCore.cxtmenu({
                    selector: 'node',
                    menuRadius: (ele:any) => { return 45 * cytoscapeCore.zoom() }, 
                    adaptativeNodeSpotlightRadius: false,
                    activePadding: 0, // additional size in pixels for the active command
                    fillColor: "#525865",
                    activeFillColor: "#B9B9B9",
                    commands: [
                        {
                            content: `<span class="pi pi-eye-slash" style="font-size:20px;"></span>`,
                            select: (node: any) => { 
                                cytoscapeCore.remove(`node[id="${HIGHLIGHT}"]`);
                                cytoscapeCore.remove(`node[id="${node.id()}"]`);
                            }
                        },
                        {
                            content: `<span class="pi pi-share-alt" style="font-size:20px;"></span>`,
                            select: async (node: any) =>  {
                                
                                const nodes = cytoscapeCore.elements().jsons()
                                        .filter((x:any) => x.group==="nodes" && x.data.id !== HIGHLIGHT)
                                        .map((x:any) => x.data.id);
                                
                                cytoscapeCore.nodes().forEach((node:any) => {
                                    node.lock();
                                });


                                const graph = await graphService.getNodeExpansion(node.id(), [], messageService!);

                                graph.nodes.forEach( (node:any) => {
                                    try{
                                        
                                        cytoscapeCore.add({ 
                                            data: { 
                                                id: node.id, 
                                                label: node.label, 
                                                color: node.color
                                            } 
                                        });
                                    } catch {

                                    }
                                    }
                                );

                                graph.links.forEach( (edge:any) => {
                                    cytoscapeCore.add({
                                        group: 'edges',
                                        data: {
                                            label: edge.label,
                                            source: edge.source, // ID of the source node
                                            target: edge.target, // ID of the target node
                                            // You can add other properties to the edge data object
                                        }
                                    })
                                })

                                cytoscapeCore.layout(layout).run(); 

                                cytoscapeCore.nodes().forEach((node:any) => {
                                    node.unlock();
                                });

                            }
                        }
                    ]
                });
            }

            // Configuration for user input events
            cytoscapeCore.on('click', 'node', async (event:any) => {
                var node = event.target;

                if (node.id() === HIGHLIGHT)
                return;

                await onNodeClickHandler(node.id());
            });
          
            cytoscapeCore.on('click', 'edge', async (event:any) => {
                await onEdgeClickHandler(event.target.id());
            });
        }
    }, [cytoscapeCore]);


   

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



    const styles: cytoscape.Stylesheet = {
        selector: 'node',
        style: {
            backgroundColor: (ele) => {return darkenHexColor(ele.data("color"), -140)},
            width: 75,
            height: 75,
            "border-width": "3px",
            "border-color": (ele) => {return ele.data("color")},
            label: (ele:any) => { return ele.data("label")}, //TODO: add maximum lenght 
            "text-valign": "center",
            "text-halign": "center",
            "color": (ele:any) => {return 'black'},  //{return getContrastColor(darkenHexColor(ele.data("color"), -140))}, TODO: make this conditional to the background
            "overlay-opacity": 0,
            // "background-opacity" : 0.5,
        }
    }

    const highlightedStyle: cytoscape.Stylesheet = {
        selector: '.highlight',
        style: {
            backgroundColor: "#00e6ff",
            "border-color": "#00e6ff",
            width: "100px",
            height: "100px",
            label: "",
            "text-valign": "center",
            "text-halign": "center",
            "color": "#ffffff",
            "overlay-opacity": 0,
            opacity: 0.2,
            "z-index": -1,
            // "background-opacity" : 0.5,
        }
    }

    const buttonStyle: cytoscape.Stylesheet = {
        selector: '.top-button',
        style: {
            shape:"polygon",

        }
    }

    const test: cytoscape.Stylesheet = {
        selector: '.infront',
        style : {

        }
    }

    const edgeStyle: cytoscape.Stylesheet = {
        selector: 'edge',
        style: {
            "text-background-opacity": 1,
            "text-background-padding": "3px",
            "text-background-color": backgroundColor,
            "line-color": () => {return edgeLineColor},
            label : (ele:any) => { return ele.data("label")},
            "text-rotation": "autorotate",
            color: edgeLabelColor,
            "curve-style": "bezier",
            'target-arrow-shape': (ele => { return ele.data("direction") !== "UNDIRECTED" ? "triangle" : "none"}),
            'target-arrow-color': edgeLineColor
        }
    }

   return <CytoscapeComponent
        cy={(cy) => { setCytoscapeCore(cy) }}
        elements={elements}
        styleEnabled={true}
        maxZoom={1}
        minZoom={0.6}
        stylesheet={[styles, edgeStyle, highlightedStyle, test, buttonStyle]}
        layout={layout}
        // "#343843"
        style={{ width: '100%', height: '100%', backgroundColor: backgroundColor }}/>

};

export default CytoscapeChart;
