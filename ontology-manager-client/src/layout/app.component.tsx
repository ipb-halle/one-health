import React from 'react';
import '../Style.scss';
import { Routes, Route } from 'react-router-dom';
import './app.component.scss';
import Layout from './layout.component';
import { CoOcurrencesSummaryPageComponent, CompoundSearchPageComponent, DataLoadPageComponent, DocumentationPageComponent, EntityTypeFormPageComponent, HomePageComponent, LinkTypeFormPageComponent, MetadataOverviewPageComponent, NeighborhoodExplorerPageComponent } from '../pages';
import GeneralSearchPageComponent from '../pages/search/general-search/general-search-page.component';

function App() {
    return (
        <div className="app">
            <Routes>
                <Route path="/" element={<Layout/>}>
                    <Route index element={<HomePageComponent/>}/>

                    
                    <Route path="entity-type-form" element={<EntityTypeFormPageComponent/>}/>
                    <Route path="entity-type-form/:id" element={<EntityTypeFormPageComponent/>}/>
                    <Route path="link-type-form" element={<LinkTypeFormPageComponent/>}/>
                    <Route path="link-type-form/:id" element={<LinkTypeFormPageComponent/>}/>
                    <Route path="ontology/overview/" element={<MetadataOverviewPageComponent/>}/>
                    <Route path="ontology/data-load/0" element={<DataLoadPageComponent/>}/>

                    <Route path="search/structure-search" element={<CompoundSearchPageComponent/>}/>
                    <Route path="search/general-search" element={<GeneralSearchPageComponent/>}/>
                
                    <Route path="neighborhood-explorer" element={<NeighborhoodExplorerPageComponent/>}/>
                    <Route path="visualization/co-ocurrence-search/" element={<CoOcurrencesSummaryPageComponent/>}/>

                    <Route path="documentation" element={<DocumentationPageComponent/>}/>
                </Route>
            </Routes>
        </div>
    );
}

export default App;
