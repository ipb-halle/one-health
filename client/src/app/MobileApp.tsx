import Layout from "@/layout/layout.component";
import { 
    DocumentationPageComponent, 
    LegalPageComponent,
    OrcidCallbackPageComponent,
 } from "@/pages";
import EntityDetailComponent from "@/pages/entity-detail/EntityDetailComponent";
import MobileHomePageComponent from "@/pages/home/mobile-home-page.component";
import { observer } from "mobx-react-lite";
import { Route, Routes } from "react-router-dom";

function MobileApp() {
    return <div className="mobile-app">
        <Routes>
            <Route path="/" element={<Layout />}>
                <Route index element={<MobileHomePageComponent />} />

                <Route
                    path="documentation"
                    element={<DocumentationPageComponent />}
                />

                <Route
                    path="legal"
                    element={<LegalPageComponent />}
                />
                <Route
                    path="detail"
                    element={<EntityDetailComponent />}
                />
                <Route
                    path="auth/orcid/callback"
                    element={<OrcidCallbackPageComponent />}
                />
            </Route>
        </Routes>
    </div>
}

export default observer(MobileApp);