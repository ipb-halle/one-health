// describes the main app component, which sets up the routing for the application
import { Link, Route, Routes } from 'react-router-dom';
import '../assets/styles/Style.scss';
import Layout from '../layout/layout.component';
import {
    CoOccurrenceSummaryPageComponent,
    DataLoadPageComponent,
    DocumentationPageComponent,
    EntityTypeFormPageComponent,
    HomePageComponent,
    LegalPageComponent,
    LinkTypeFormPageComponent,
    MetadataOverviewPageComponent,
    NeighborhoodExplorerPageComponent,
} from '../pages';
import CompoundSearchPageComponent from '../pages/compound-search/compound-search-page.component';
import './app.component.scss';
import { RootStoreContext, StoreProvider } from './providers/store-provider';
import { useContext, useEffect } from 'react';
import { DesktopApp } from './DesktopApp';
import { observer } from 'mobx-react-lite';
import MobileApp from './MobileApp';

function App() {

    const screenDeviceStore = useContext(RootStoreContext).screenDeviceStore;

    useEffect(
        () => {
            screenDeviceStore.start();
            return () => screenDeviceStore.stop();
        },
        [screenDeviceStore]);

    const appComponent = screenDeviceStore.isMobile ?
        <MobileApp /> :
        <DesktopApp />

    return (
        <StoreProvider>
            {appComponent}
        </StoreProvider>
    );
}

export default observer(App);
