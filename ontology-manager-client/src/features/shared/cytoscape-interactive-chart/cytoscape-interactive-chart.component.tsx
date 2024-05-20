import { Component } from "react";
import CytoscapeComponent from "react-cytoscapejs";
import cytoscape, { CoseLayoutOptions } from "cytoscape";
import cxtmenu from 'cytoscape-cxtmenu';
import Cytoscape from 'cytoscape';
import { MessageService } from "../messages";
import { GraphService } from "../../../services";
import { darkenHexColor } from "../../../utils";

Cytoscape.use(cxtmenu);



const React = require('react');



interface CytoscapeInteractiveChartProps{
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
    messageService: MessageService;
};

const HIGHLIGHT = "__highlight";

class CytoscapeInteractiveChartComponent extends Component<CytoscapeInteractiveChartProps> { 

    messageService : MessageService | undefined;
    graphService : GraphService;
    cytoscapeCore: any;

    selectedNodes : any[] = [];
    currentSelectedNode: number = 0;

    lockedNodes: Map<string, boolean> = new Map();



    constructor(props: CytoscapeInteractiveChartProps) {
        super(props);

        this.graphService = props.graphService;
        this.messageService = props.messageService;
        // Initialize state if needed

    }

    findNode (query: string) {
        console.log(query);
        console.log("cojonesssssss");
        this.cytoscapeCore.nodes().forEach((node:any) => {
            var label = node.data('label').toLowerCase();
            if (label.includes(query.toLowerCase())) {
                node.addClass("selected"); 
                //node.style('background-color', 'yellow');  // Highlight matching nodes
                this.selectedNodes.push(node);
            } else {
    
            }
        });

        if (this.selectedNodes.length > 0){
            this.cytoscapeCore.center(this.selectedNodes[0]);
            this.currentSelectedNode = 0;
        }
        
    }

    nextSelectedNode(){
        if (this.selectedNodes.length <= 0)
            return;

        if (this.currentSelectedNode >= this.selectedNodes.length - 1)
            this.currentSelectedNode = 0;
        else this.currentSelectedNode += 1;

        this.cytoscapeCore.center(this.selectedNodes[this.currentSelectedNode]);

    }

    previousSelectedNode() {
        if (this.selectedNodes.length <= 0)
            return;

        if (this.currentSelectedNode <= 0)
            this.currentSelectedNode = this.selectedNodes.length - 1;
        else this.currentSelectedNode -= 1;

        this.cytoscapeCore.center(this.selectedNodes[this.currentSelectedNode]);
    }


    downloadPNG(){
        var pngData = this.cytoscapeCore.png();
      var link = document.createElement('a');
      link.href = pngData;
      link.download = 'graph.png';
      link.click();
    }

    downloadJSON(){
        var cyJson = this.cytoscapeCore.json();
      
      // Convert JSON object to string
      var jsonString = JSON.stringify(cyJson, null, 2); // Pretty-print with 2-space indentation
      
      // Create a blob from the JSON string
      var blob = new Blob([jsonString], { type: 'application/json' });
      
      // Create a link element
      var link = document.createElement('a');
      
      // Set link's href to point to the Blob URL
      link.href = URL.createObjectURL(blob);
      link.download = 'graph.json';
      
      // Append link to the body (required for Firefox)
      document.body.appendChild(link);
      
      // Trigger the download by simulating a click
      link.click();
      
      // Remove link from the body
      document.body.removeChild(link);
    }


    resetView(){
        this.cytoscapeCore.fit();
    }

    redoLayout(){
        this.cytoscapeCore.layout(this.layout).run(); 
    }

    componentDidMount(): void {
        this.configureCytoscape(this.cytoscapeCore);
    }


    reset(): void {
        this.selectedNodes = [];
        this.currentSelectedNode = 0;
        this.lockedNodes = new Map();
        this.cytoscapeCore.remove(this.cytoscapeCore.elements());
    }

    clean(): void {
        this.cytoscapeCore.nodes().forEach((n:any) => {
            if (!this.lockedNodes.has(n.id()))
                this.cytoscapeCore.remove(n);
        })
    }

    getElements() {
        const elements = this.cytoscapeCore.json().elements;
        return JSON.stringify(elements);
    }

    setElements(elements:any) {
        this.reset();
        const parsed = JSON.parse(elements);
        this.cytoscapeCore.add(parsed);
    }

    configureCytoscape (cytoscapeCore: any) {
        const contextMenu = this.props.contextMenu;

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
                    menuRadius: (ele:any) => { return 45 * (2 - cytoscapeCore.zoom()) }, 
                    adaptativeNodeSpotlightRadius: false,
                    activePadding: 0, // additional size in pixels for the active command
                    fillColor: "#525865",
                    activeFillColor: "#B9B9B9",
                    commands: [
                        {
                            content: `<span class="pi pi-eye-slash" style="font-size:20px;"></span>`,
                            select: (node: any) => { 
                                if (this.lockedNodes.has(node.id())) return;
                                cytoscapeCore.remove(`node[id="${HIGHLIGHT}"]`);
                                cytoscapeCore.remove(`node[id="${node.id()}"]`);
                            }
                        },
                        {
                            content: `<span class="fa fa-lock" style="font-size:20px;"></span>`,
                            select: (node: any) => { 
                                if (this.lockedNodes.has(node.id()))
                                    this.lockedNodes.delete(node.id());
                                else this.lockedNodes.set(node.id(), true);
                            }
                        },
                        {
                            content: `<span class="fa fa-minimize" style="font-size:20px;"></span>`,
                            select: (node: any) => { 

                                const inNodes = cytoscapeCore.elements().jsons()
                                        .filter((x:any) => x.group==="edges" && x.data.source === node.id())
                                        .map((x:any) => x.data.target);
                                
                                const outNodes = cytoscapeCore.elements().jsons()
                                        .filter((x:any) => x.group==="edges" && x.data.target === node.id())
                                        .map((x:any) => x.data.source);
                                
                                inNodes.forEach((id:string) => {
                                    if (!this.lockedNodes.has(id))
                                        cytoscapeCore.remove(`node[id="${id}"]`);
                                });

                                outNodes.forEach((id:string) => {
                                    if (!this.lockedNodes.has(id))
                                        cytoscapeCore.remove(`node[id="${id}"]`);
                                });
                            }
                        },
                        {
                            content: `<span class="fa fa-maximize" style="font-size:20px;"></span>`,
                            select: async (node: any) =>  {
                                
                                const nodes = cytoscapeCore.elements().jsons()
                                        .filter((x:any) => x.group==="nodes" && x.data.id !== HIGHLIGHT)
                                        .map((x:any) => x.data.id);
                                
                                // cytoscapeCore.nodes().forEach((node:any) => {
                                //     node.lock();
                                // });


                                const graph = await this.graphService.getNodeExpansion(node.id(), nodes, this.messageService!);

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

                                cytoscapeCore.layout(this.layout).run(); 

                                // cytoscapeCore.nodes().forEach((node:any) => {
                                //     node.unlock();
                                // });

                            }
                        }
                    ]
                });
            }

            cytoscapeCore.on('tap', (event:any) => {
                if (event.target === cytoscapeCore) {
                  console.log('Tap on empty space');
                  // You can execute any logic you need here
                  this.selectedNodes.forEach((node:any) => {
                    node.removeClass("selected"); 
                  });
                  this.selectedNodes = [];
                }
              });

            // Configuration for user input events
            cytoscapeCore.on('click', 'node', async (event:any) => {
                var node = event.target;

                if (node.id() === HIGHLIGHT)
                return;

                await this.props.onNodeClickHandler(node.id());
            });

          
            cytoscapeCore.on('click', 'edge', async (event:any) => {
                await this.props.onEdgeClickHandler(event.target.id());
            });
        }
    }


   

    layout: CoseLayoutOptions  = {
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



    styles: cytoscape.Stylesheet = {
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

    highlightedStyle: cytoscape.Stylesheet = {
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

    selectedStyle: cytoscape.Stylesheet = {
        selector: '.selected',
        style: {
            backgroundColor: 'yellow'
        }
    }

    buttonStyle: cytoscape.Stylesheet = {
        selector: '.top-button',
        style: {
            shape:"polygon",

        }
    }

    test: cytoscape.Stylesheet = {
        selector: '.infront',
        style : {

        }
    }

    edgeStyle: cytoscape.Stylesheet = {
        selector: 'edge',
        style: {
            "text-background-opacity": 1,
            "text-background-padding": "3px",
            "text-background-color": this.props.backgroundColor,
            "line-color": () => {return this.props.edgeLineColor},
            // label : (ele:any) => { return ele.data("label")},
            'width': 2,
            "text-rotation": "autorotate",
            color: this.props.edgeLabelColor,
            "curve-style": "bezier",
            'target-arrow-shape': (ele => { return ele.data("direction") !== "UNDIRECTED" ? "triangle" : "none"}),
            'target-arrow-color': this.props.edgeLineColor
        }
    }

    render() {

        return <CytoscapeComponent
             cy={(cy) => { this.cytoscapeCore = cy }}
             elements={this.props.elements}
             styleEnabled={true}
             maxZoom={1}
             stylesheet={[this.styles, this.edgeStyle, this.highlightedStyle, this.test, this.buttonStyle, this.selectedStyle]}
             layout={this.layout}
             // "#343843"
             style={{ width: '100%', height: '100%', backgroundColor: this.props.backgroundColor }}/>
    }

};

export default CytoscapeInteractiveChartComponent;
