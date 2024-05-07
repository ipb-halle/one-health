
import ElementView from './element-view.component';
import { useCallback, useContext, useEffect, useRef, useState } from 'react';

import './explorer.component.scss';
import { dependencyFactory } from '../../injection/inversify.config';
import { IMetadataService } from '../../services/metadata-service';
import { SERVICE_TYPES } from '../../services';
import { RefObject } from 'react';
import { Toast } from 'primereact/toast';
import CytoscapeChart from './cytoscape-chart.component';
import { GraphService } from '../../services/interfaces/graph-service';
import { MessageServiceContext } from '../../messages';
const React = require('react');

const MemoChart = React.memo(CytoscapeChart);

interface GraphExplorerProps{
    graphService: GraphService; //TODO: make an interface GetInitial, GetNodeExpansion, make option to group edges or not in the api call
};


const Explorer: React.FC<GraphExplorerProps> = ({graphService}) => {
    const toast : RefObject<Toast> = useRef<Toast>(null);
    const [element, setElement] = useState<any>({});
    const { messageService } = useContext(MessageServiceContext);





    
    const [elements, setElements] = useState<any>([]);

    const init = async () => {
        let graph = await graphService.getInitial(messageService!);
        const newElements = [...graph.nodes.map((x:any)=>{ return {data: x}}), ...graph.links?.map((x:any)=>{ return {data: x}})];
        setElements([...newElements]);
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
  

    return (
        <div className="explorer">
                   
                <div
                    className="row"
                    style={{ height: '600px', width: '100%', margin: '0px' }}
                >
                    <div className="col-md-9" style={{ padding: '0px' }}>
                        <MemoChart 
                            elements={elements} 
                            contextMenu={true}
                            onNodeClickHandler={onNodeClickHandler} 
                            onEdgeClickHandler={onEdgeClickHandler}
                            onNodeExpandHandler={onNodeExpandHandler}
                            onNodeHideHandler={onNodeHideHandler} 
                            backgroundColor="white"
                            edgeLineColor="black"
                            edgeLabelColor="black"                           
                            >
                        </MemoChart>
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
