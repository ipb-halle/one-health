import Layout from "@/layout/layout.component";
import { CoOccurrenceSummaryPageComponent, DocumentationPageComponent, HomePageComponent, LegalPageComponent, NeighborhoodExplorerPageComponent } from "@/pages";
import CompoundSearchPageComponent from "@/pages/compound-search/compound-search-page.component";
import { Link, Route, Routes } from "react-router-dom";


export function DesktopApp() {

    return <div className="app">
        <nav>
            <Link to="/">Home</Link> |{' '}
            <Link to="/neighborhood-explorer">
                Neighborhood Explorer
            </Link>{' '}
            |{' '}
            <Link to="/visualization/co-occurrence-search">
                Co-Occurrences
            </Link>{' '}
            | <Link to="/documentation">Documentation</Link> |{' '}
            <Link to="/legal">Legal Information</Link> |{' '}
        </nav>

        <Routes>
            <Route path="/" element={<Layout />}>
                <Route index element={<HomePageComponent />} />

                <Route
                    path="search/structure-search"
                    element={<CompoundSearchPageComponent />}
                />
                <Route
                    path="neighborhood-explorer"
                    element={<NeighborhoodExplorerPageComponent />}
                />
                <Route
                    path="visualization/co-occurrence-search"
                    element={<CoOccurrenceSummaryPageComponent />}
                />

                <Route
                    path="documentation"
                    element={<DocumentationPageComponent />}
                />

                <Route
                    path="legal"
                    element={<LegalPageComponent />}
                />
            </Route>
        </Routes>
    </div>

}



