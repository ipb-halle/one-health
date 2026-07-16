import Footer from "@/layout/footer.component";
import Layout from "@/layout/layout.component";
import { DocumentationPageComponent, LegalPageComponent } from "@/pages";
import MobileHomePageComponent from "@/pages/home/mobile-home-page.component";
import { observer } from "mobx-react-lite";
import { Link, Route, Routes } from "react-router-dom";

function MobileApp() {
    return <div className="mobile-app">
        <nav hidden={true}>
            <Link to="/">Home</Link> |{' '}
            |{' '}
            | <Link to="/documentation">Documentation</Link> |{' '}
            <Link to="/legal">Legal Information</Link> |{' '}
        </nav>

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
            </Route>
        </Routes>
    </div>
}

export default observer(MobileApp);