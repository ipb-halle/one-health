import React from 'react';
import Header from './header.component';
import Footer from './footer.component';
import '../../Style.scss';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import { NotFound, Documentation, Home } from '../pages';
import './app.component.scss';
import { EntityTypeForm } from '../ontology/entity-types';
import { LinkTypeForm } from '../ontology/link-types';
import OntologyOverview from '../ontology/ontology-overview.component';
import DataLoader from '../ontology/data-load/data-load.component';
import { CoOcurrenceSearch } from '../data-visualization/co-ocurrence-search';
import EntityTypePage from '../ontology/entity-types/entity-type-page.component';
import FiltersDemo from '../filter/demo';
import GraphExplorer from '../ontology/graph/explorer.component';
import ContactPage from '../pages/contact.component';

function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Header></Header>

                <div className="content">
                    <Routes>
                        <Route path="/" Component={Home} />
                        <Route
                            path="/contact"
                            Component={ContactPage}
                        />
                        <Route
                            path="/documentation"
                            Component={Documentation}
                        />
                        <Route
                            path="/entity-type-form"
                            Component={EntityTypeForm}
                        />
                        <Route
                            path="/link-type-form"
                            Component={LinkTypeForm}
                        />
                        <Route
                            path="/ontology/overview/"
                            Component={OntologyOverview}
                        />
                        <Route
                            path="/ontology/data-load/0"
                            Component={DataLoader}
                        />
                        <Route
                            path="/visualization/co-ocurrence-search/"
                            Component={CoOcurrenceSearch}
                        />
                        <Route path="/entity-type-form/:id" Component={EntityTypeForm}/>
                        <Route path="/link-type-form/:id" Component={LinkTypeForm}/>
                        <Route path="/neighborhood-explorer" Component={GraphExplorer}/>
                        <Route path="/test" Component={FiltersDemo}></Route>

                    </Routes>
                
                </div>
                
                <Footer></Footer>
            </BrowserRouter>
        </div>
    );
}

export default App;
