import React, { useContext, useEffect, useState } from 'react';
import { Button } from 'primereact/button';
import './OntologyOverview.scss';
import OverviewCard from './overview-card.component';
import { IGridSettings, IGridColumn, Grid } from '../utils/grid';

import {
    faArrowRightArrowLeft,
    faCube,
    faDatabase,
    faHeartPulse,
} from '@fortawesome/free-solid-svg-icons';
import { Explorer } from './explorer';
import { dependencyFactory } from '../injection/inversify.config';
import { IMetadataService } from '../services/metadata-service';
import { SERVICE_TYPES } from '../services';
import { RefObject } from 'react';
import { useRef } from 'react';
import { Toast } from 'primereact/toast';
import { Panel } from 'primereact/panel';
import EntityTypePage from './entity-types/entity-type-page.component';
import { SelectButton } from 'primereact/selectbutton';
import LinkTypePage from './link-types/link-type-page.component';
import { Link } from 'react-router-dom';
import { MessageServiceContext } from '../messages';


interface HeaderProps {
    title: string;
}

const gridSettings: IGridSettings = {
    editingEnabled: true,
    deletingEnabled: true,
};

const gridColumns: IGridColumn[] = [
    {
        key: '',
        header: 'Name',
    },
    {
        key: '',
        header: 'Description',
    },
];


const a = (
    <>
        <Button label="Explore" icon="pi pi-compass" />
        <Button
            label="Create"
            severity="secondary"
            icon="pi pi-plus"
            style={{ marginLeft: '0.5em' }}
        />
    </>
);

const OntologyOverview: React.FC = () => {
    const metadataService = dependencyFactory.get<IMetadataService>(SERVICE_TYPES.IMetadataService);
    const {messageService} = useContext(MessageServiceContext);

    const [summary, setSummary] = useState<IMetadataSummary>({
        entityTypesCount: 0,
        entitiesCount: 0,
        linkTypesCount: 0,
        linkCounts: 0,
        dataSourcesCount: 0,
        healthProblemsCount: 0
    });

    const [selectedPanel, setSelectedPanel] = useState<string>("ontology");

    

    const init = async () => {
        setSummary(await metadataService.getSummary(messageService!));
        console.log(summary);
    }

    useEffect(() => {
        init();
    },[])


    const panelOptions = [
        {icon: 'pi pi-share-alt', value: 'ontology'},
        {icon: 'pi pi-box', value: 'entity'},
        {icon: 'pi pi-arrows-h', value: 'link'},
        {icon: 'pi pi-database', value: 'datasets'}
    ];

    const panelOptionsTemplate = (option:any) => {
        return <i className={option.icon}></i>;
    }

    const getIcons = () => {
        return <SelectButton value={selectedPanel} onChange={(e) => setSelectedPanel(e.value)} itemTemplate={panelOptionsTemplate}  optionLabel="value" options={panelOptions} />
         
            
    }

    const entityCardFooter = (
         <Link to="/entity-type-form">
        <Button
        label="Create"
        severity="secondary"
        icon="pi pi-plus"
        style={{ marginLeft: '0.5em' }}
        />
        </Link>
    );

    const linkCardFooter = (
        <Link to="/link-type-form">
       <Button
       label="Create"
       severity="secondary"
       icon="pi pi-plus"
       style={{ marginLeft: '0.5em' }}
       />
       </Link>
   );


    return (
        <div className="container">
            <div className="row mb-3">
                <div className="col pt-1" style={{ maxWidth: '50px' }}>
                    <i
                        className="pi pi-chart-line"
                        style={{ fontSize: '32px' }}
                    ></i>
                </div>
                <div className="col-sm-3">
                    <h2>Overview</h2>
                </div>
            </div>

            <div className="row justify-content-center mb-3">
                <div className="col">
                    <OverviewCard
                        leftCount={summary.entitiesCount}
                        leftLabel="Entities"
                        rightCount={summary.entityTypesCount}
                        rightLabel="Types"
                        icon={faCube}
                        actions={entityCardFooter}
                    ></OverviewCard>
                </div>
                <div className="col">
                    <OverviewCard
                        leftCount={summary.linkCounts}
                        leftLabel="Links"
                        rightCount={summary.linkTypesCount}
                        rightLabel="Types"
                        icon={faArrowRightArrowLeft}
                        actions={linkCardFooter}
                    ></OverviewCard>
                </div>
                <div className="col">
                    <OverviewCard
                        leftCount={summary.healthProblemsCount}
                        leftLabel="Issues"
                        rightCount={summary.dataSourcesCount}
                        rightLabel="Data Sources"
                        icon={faDatabase}
                        actions={a}
                    ></OverviewCard>
                </div>
            </div>

            <Panel header="Explorer" className='panel-no-padding'>
                <Explorer graphService={metadataService}></Explorer>
            </Panel>
        </div>
    );
};

export default OntologyOverview;
