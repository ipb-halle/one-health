import { dependencyFactory } from "../../injection/inversify.config";
import { ILinkTypeService } from "../../services";
import { SERVICE_TYPES } from "../../services";
import { RefObject, useState } from "react";
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

Cytoscape.use(cxtmenu);



const React = require('react');



interface CytoscapeChartProps{
    elements:any;
    onNodeClickHandler:any;
    onEdgeClickHandler:any;
};

const HIGHLIGHT = "__highlight";
const BUTTON = "__button";

const CytoscapeChart: React.FC<CytoscapeChartProps> = ({elements, onNodeClickHandler, onEdgeClickHandler}) => {
    const [cytoscapeCore, setCytoscapeCore] = useState<any>();
    const background = "#343843";
    let menu = {};


    let defaults = {
        menuRadius: (ele:any) => {  console.log("repingolaaa");console.log(ele); return 100; }, // the outer radius (node center to the end of the menu) in pixels. It is added to the rendered size of the node. Can either be a number or function as in the example.
        selector: 'node', // elements matching this Cytoscape.js selector will trigger cxtmenus
        commands: [ // an array of commands to list in the menu or a function that returns the array
          /*
          { // example command
            fillColor: 'rgba(200, 200, 200, 0.75)', // optional: custom background color for item
            content: 'a command name', // html/text content to be displayed in the menu
            contentStyle: {}, // css key:value pairs to set the command's css in js if you want
            select: function(ele){ // a function to execute when the command is selected
              console.log( ele.id() ) // `ele` holds the reference to the active element
            },
            hover: function(ele){ // a function to execute when the command is hovered
              console.log( ele.id() ) // `ele` holds the reference to the active element
            },
            enabled: true // whether the command is selectable
          }
          */
        ], // function( ele ){ return [ /*...*/ ] }, // a function that returns commands or a promise of commands
        fillColor: 'rgba(0, 0, 0, 0.75)', // the background colour of the menu
        activeFillColor: 'rgba(1, 105, 217, 0.75)', // the colour used to indicate the selected command
        activePadding: 20, // additional size in pixels for the active command
        indicatorSize: 24, // the size in pixels of the pointer to the active command, will default to the node size if the node size is smaller than the indicator size, 
        separatorWidth: 3, // the empty spacing in pixels between successive commands
        spotlightPadding: 4, // extra spacing in pixels between the element and the spotlight
        adaptativeNodeSpotlightRadius: false, // specify whether the spotlight radius should adapt to the node size
        minSpotlightRadius: 24, // the minimum radius in pixels of the spotlight (ignored for the node if adaptativeNodeSpotlightRadius is enabled but still used for the edge & background)
        maxSpotlightRadius: 38, // the maximum radius in pixels of the spotlight (ignored for the node if adaptativeNodeSpotlightRadius is enabled but still used for the edge & background)
        openMenuEvents: 'cxttapstart taphold', // space-separated cytoscape events that will open the menu; only `cxttapstart` and/or `taphold` work here
        itemColor: 'white', // the colour of text in the command's content
        itemTextShadowColor: 'transparent', // the text shadow colour of the command's content
        zIndex: 9999, // the z-index of the ui div
        atMouse: false, // draw menu at mouse position
        outsideMenuCancel: false // if set to a number, this will cancel the command if the pointer is released outside of the spotlight, padded by the number given 
      };


    useEffect(() => {
        

        if (cytoscapeCore){

            cytoscapeCore.cxtmenu({
                menuRadius: (ele:any) => { return 45 * cytoscapeCore.zoom() }, // the outer radius (node center to the end of the menu) in pixels. It is added to the rendered size of the node. Can either be a number or function as in the example.

                selector: 'node',
                adaptativeNodeSpotlightRadius: false,
                activePadding: 0, // additional size in pixels for the active command
                fillColor: "#525865",
                activeFillColor: "#B9B9B9",
                commands: [
                    {
                        content: `<span class="pi pi-eye-slash" style="font-size:20px;"></span>`,
                        select: function(ele:any){
                            console.log( ele.id() );
                        }
                    },


                    {
                        content: `<span class="pi pi-share-alt" style="font-size:20px;"></span>`,
                        select: function(ele:any){
                            console.log( ele.position() );
                        }
                    }
                ]
            });

            
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

            console.log("out");
          });

          cytoscapeCore.on('click', 'node', async (event:any) => {
            // setSelectedElementId({id: event.target.id(), type: "entity"});
            // setElement(await metadataService.getEntityType(event.target.id(), toast));
            var node = event.target;
            if (node.id() === HIGHLIGHT)
            return;

            // var button = cytoscapeCore.add ({
            //     position: node.position(),
            //     data: {
            //         id: BUTTON,
            //         label: "",
            //         color:"#ffffff"
            //     }
            // });
            // button.addClass("top-button");
            await onNodeClickHandler(node.id());




          });
          
          // Add click event listener to edges
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
            label: (ele:any) => { return ele.data("label")},
            "text-valign": "center",
            "text-halign": "center",
            "color": (ele:any) => {return getContrastColor(darkenHexColor(ele.data("color"), -140))},
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

    // const highlightedStyles: cytoscape.Stylesheet = {
    //     selector: '.highlighted',
    //     style: {
    //         // 'overlay-opacity': 0.2,
    //         // 'overlay-color': '#F8F9FA',
    //         // 'overlay-padding': '7px'
    //         "border-color": "#00eaff",
    //         "border-opacity": 0.25,
    //         "border-width": "13px",

    //     }
    // }

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



   return <CytoscapeComponent
   cy={(cy) => { setCytoscapeCore(cy) }}
   elements={elements}
   styleEnabled={true}
   maxZoom={1}
   minZoom={0.6}
   stylesheet={[styles, edgeStyle, highlightedStyle, test, buttonStyle]}
   layout={layout}
   // "#343843"
   style={{ width: '100%', height: '100%', backgroundColor: background }}
/>
};

export default CytoscapeChart;
