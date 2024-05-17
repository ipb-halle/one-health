import { dependencyFactory } from "../../injection/inversify.config";
import { IEntityTypeService, SERVICE_TYPES } from "../../services";
import LazyLoadGrid from "../../utils/grid/grid.component";
import { Column } from "primereact/column";
import { RefObject, useContext, useState } from "react";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import { Panel } from "primereact/panel";
import { PageTitle } from "../../layout";
import CytoscapeChart from "../explorer/cytoscape-chart.component";
import { ElementView, Explorer } from "../explorer";
import { MessageServiceContext } from "../../messages";
import { IEntityService } from "../../services/entity-service";


const React = require('react');


const MemoChart = React.memo(CytoscapeChart);


const GraphExplorer: React.FC = () => {
    const [element, setElement] = useState<any>({});
    const entityService = dependencyFactory.get<IEntityService>(SERVICE_TYPES.IEntityService);
    const { messageService } = useContext(MessageServiceContext);


    return (
        <div className="container">
            <PageTitle icon="fa fa-compass" title="Neighborhood Explorer" help={true}></PageTitle>
            <Panel header="Explorer" className='panel-no-padding'>
            
            <Explorer graphService={entityService}></Explorer>

            </Panel>


        </div>
    );
    
};

export default GraphExplorer;
