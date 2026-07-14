import Footer from "@/layout/footer.component";
import GeneralSearchInput from "@/shared/components/GeneralSearchInput";
import { observer } from "mobx-react-lite";

function MobileApp() {
    return <div className="mobile-app">
        <div>header</div>
        <div>grafik</div>
        <div>Title text</div>
        <GeneralSearchInput />
        <div>results</div>
        <div>stats</div>
        <Footer />
    </div>
}

export default observer(MobileApp);