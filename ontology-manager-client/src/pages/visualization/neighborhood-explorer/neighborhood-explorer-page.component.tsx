
import { useContext, useState } from "react";
import { CytoscapeInteractiveChartComponent } from "../../../features/shared/cytoscape-interactive-chart";
import { dependencyFactory } from "../../../features/shared/injection";
import { IEntityService, SERVICES } from "../../../services";
import { MessageServiceContext } from "../../../features/shared/messages";
import { PageTitle } from "../../../components";
import { Panel } from "primereact/panel";
import { NeighborhoodExplorerComponent } from "../../../features/modules/visualization";

const React = require('react');


const MemoChart = React.memo(CytoscapeInteractiveChartComponent);


const NeighborhoodExplorerPageComponent: React.FC = () => {
    const [element, setElement] = useState<any>({});
    const entityService = dependencyFactory.get<IEntityService>(SERVICES.IEntityService);
    const { messageService } = useContext(MessageServiceContext);


    return (
        <div className="container">
            <PageTitle icon="fa fa-compass" title="Neighborhood Explorer" help={true}></PageTitle>
            <Panel header="Explorer" className='panel-no-padding'>
            
            <NeighborhoodExplorerComponent graphService={entityService}/>

            </Panel>


        </div>
    );
    
};

export default  NeighborhoodExplorerPageComponent;
