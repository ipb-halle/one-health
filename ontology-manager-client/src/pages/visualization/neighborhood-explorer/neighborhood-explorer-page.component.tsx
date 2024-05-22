
import { useContext, useState } from "react";
import { CytoscapeInteractiveChartComponent } from "../../../features/shared/cytoscape-interactive-chart";
import { dependencyFactory } from "../../../features/shared/injection";
import { IEntityService, SERVICES } from "../../../services";
import { MessageServiceContext } from "../../../features/shared/messages";
import { PageTitle } from "../../../components";
import { Panel } from "primereact/panel";
import { NeighborhoodExplorerComponent } from "../../../features/modules/visualization";
import NeighborhoodExplorerTour from "../../../features/modules/visualization/neighborhood-explorer/neighborhood-explorer-tour.component";

const React = require('react');


const MemoChart = React.memo(CytoscapeInteractiveChartComponent);


const NeighborhoodExplorerPageComponent: React.FC = () => {
    const [element, setElement] = useState<any>({});
    const entityService = dependencyFactory.get<IEntityService>(SERVICES.IEntityService);
    const { messageService } = useContext(MessageServiceContext);

    const [runTutorial, setRunTutorial] = useState<boolean>(false);

    const helpClickedHandler = () => {
        setRunTutorial(true);
    }

    const helpTourCallback = () => {
        setRunTutorial(false);
    }


    return (
        <div className='page-container'>
            <PageTitle icon="fa fa-compass" title="Neighborhood Explorer" help={true} helpClickedHandler={helpClickedHandler}></PageTitle>

            <NeighborhoodExplorerTour run={runTutorial} callback={helpTourCallback}></NeighborhoodExplorerTour>
            
            <NeighborhoodExplorerComponent graphService={entityService}/>



        </div>
    );
    
};

export default  NeighborhoodExplorerPageComponent;
