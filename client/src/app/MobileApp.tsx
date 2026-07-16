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
            <Link to="/imprint">Imprint</Link> |{' '}
            <Link to="/privacy">Privacy</Link> |{' '}
            <Link to="/accessibility">Accessibility</Link>
        </nav>

        <Routes>
            <Route path="/" element={<Layout />}>
                <Route index element={<MobileHomePageComponent />} />

                <Route
                    path="documentation"
                    element={<DocumentationPageComponent />}
                />

                <Route
                    path="imprint"
                    element={<LegalPageComponent activeIndex={0} />}
                />
                <Route
                    path="privacy"
                    element={<LegalPageComponent activeIndex={1} />}
                />
                <Route
                    path="accessibility"
                    element={<LegalPageComponent activeIndex={2} />}
                />
            </Route>
        </Routes>

        <Footer />
    </div>
}

export default observer(MobileApp);