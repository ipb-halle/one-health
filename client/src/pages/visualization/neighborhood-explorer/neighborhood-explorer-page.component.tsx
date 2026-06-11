import { useContext, useEffect, useState } from 'react';
import CytoscapeInteractiveChartComponent from '../../../features/visualization/neighborhood-explorer/components/cytoscape-interactive-chart.component';
import { dependencyFactory } from '../../../app/di';
import { SERVICES } from '@/app/di/service-types';
import { IEntityService } from '@/features/visualization/neighborhood-explorer/services/entity-service';
import MessageServiceContext from '../../../app/providers/messages/message-service.context';
import { PageTitle } from '../../../shared/components';
import NeighborhoodExplorerComponent from '../../../features/visualization/neighborhood-explorer/components/neighborhood-explorer.component';
import NeighborhoodExplorerTour from '../../../features/visualization/neighborhood-explorer/components/neighborhood-explorer-tour.component';
import {
    ILocalStorageStore,
    ITutorialStore,
    LOCAL_STORAGE_KEYS,
    STORES,
} from '../../../store/inversify';
import { confirmDialog } from 'primereact/confirmdialog';
import { useNavigate } from 'react-router-dom';
import { toolDisclaimer } from '../../../shared';

import React from 'react';

const MemoChart = React.memo(CytoscapeInteractiveChartComponent);

const NeighborhoodExplorerPageComponent: React.FC = () => {
    const [element, setElement] = useState<any>({});
    const entityService = dependencyFactory.get<IEntityService>(
        SERVICES.IEntityService,
    );
    const tutorialStore = dependencyFactory.get<ITutorialStore>(
        STORES.ITutorialStore,
    );
    const { messageService } = useContext(MessageServiceContext);
    const localStorageStore = dependencyFactory.get<ILocalStorageStore>(
        STORES.ILocalStorageStore,
    );
    const navigate = useNavigate();

    const [runTutorial, setRunTutorial] = useState<boolean>(
        localStorageStore.getBooleanKeyValue(
            LOCAL_STORAGE_KEYS.showNeighborhoodExplorerTutorial,
        ),
    );

    const helpClickedHandler = () => {
        setRunTutorial(true);
    };

    const helpTourCallback = () => {
        setRunTutorial(false);
        localStorageStore.setBooleanKeyValue(
            LOCAL_STORAGE_KEYS.showNeighborhoodExplorerTutorial,
            false,
        );
    };

    useEffect(() => {
        if (
            localStorageStore.getBooleanKeyValue(
                LOCAL_STORAGE_KEYS.showNeighborhoodExplorerWarning,
            )
        )
            confirmTermsAndConditions();
    }, []);

    const confirmTermsAndConditions = () => {
        confirmDialog({
            header: 'Disclaimer',
            icon: 'pi pi-exclamation-triangle',
            message: toolDisclaimer,
            acceptLabel: 'Understood',
            rejectLabel: 'Back',
            accept: () => {
                localStorageStore.setBooleanKeyValue(
                    LOCAL_STORAGE_KEYS.showNeighborhoodExplorerWarning,
                    false,
                );
            },
            reject: () => {
                navigate('/');
            },
            closable: false,
        });
    };

    return (
        <div className="page-container-wide">
            <PageTitle
                icon="fa fa-compass"
                title="Neighborhood Explorer"
                help={true}
                helpClickedHandler={helpClickedHandler}></PageTitle>

            <NeighborhoodExplorerTour
                run={runTutorial}
                callback={helpTourCallback}></NeighborhoodExplorerTour>

            <NeighborhoodExplorerComponent graphService={entityService} />
        </div>
    );
};

export default NeighborhoodExplorerPageComponent;
