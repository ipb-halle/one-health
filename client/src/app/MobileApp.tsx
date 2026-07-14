import { observer } from "mobx-react-lite";

function MobileApp() {
    return <div>
        <div>header</div>
        <div>grafik</div>
        <div>Title text</div>
        <div>quick search</div>
        <div>results</div>
        <div>stats</div>
        <div>footer</div>
    </div>
}

export default observer(MobileApp);