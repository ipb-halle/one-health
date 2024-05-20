import React from 'react';
import '../../Style.scss';
import { Routes, Route } from 'react-router-dom';

import { Documentation, Home } from '../pages';
import './app.component.scss';
import { EntityTypeForm } from '../ontology/entity-types';
import { LinkTypeForm } from '../ontology/link-types';
import OntologyOverview from '../ontology/ontology-overview.component';
import DataLoader from '../ontology/data-load/data-load.component';
import { CoOcurrenceSearch } from '../data-visualization/co-ocurrence-search';
import FiltersDemo from '../filter/demo';
import GraphExplorer from '../ontology/graph/explorer.component';
import ContactPage from '../pages/contact.component';
import StructureSearchPage from '../search/structure-search/structure-search.component';
import Layout from './layout.component';

function App() {
    return (
        <div className="App">
          
                    <Routes>
                        <Route path="/" element={<Layout/>}>
                            <Route index Component={Home}></Route>
                            <Route
                                path="contact"
                                Component={ContactPage}
                            />
                            <Route
                                path="documentation"
                                Component={Documentation}
                            />
                            <Route
                                path="entity-type-form"
                                Component={EntityTypeForm}
                            />
                            <Route
                                path="link-type-form"
                                Component={LinkTypeForm}
                            />
                            <Route
                                path="ontology/overview/"
                                Component={OntologyOverview}
                            />
                            <Route
                                path="ontology/data-load/0"
                                Component={DataLoader}
                            />
                            <Route
                                path="visualization/co-ocurrence-search/"
                                Component={CoOcurrenceSearch}
                            />
                            <Route path="entity-type-form/:id" Component={EntityTypeForm}/>
                            <Route path="link-type-form/:id" Component={LinkTypeForm}/>
                            <Route path="neighborhood-explorer" Component={GraphExplorer}/>
                            <Route path="test" Component={FiltersDemo}></Route>
                            <Route path="search/structure-search" Component={StructureSearchPage}></Route>
                        </Route>
                    </Routes>
        </div>
    );
}

export default App;
