import { RootStoreContext } from "@/app/providers/store-provider";
import { observer } from "mobx-react-lite"
import { useContext } from "react"

function EntityDetailComponent() {
    const entityDetailStore = useContext(RootStoreContext).entityDetailStore;
    if (!entityDetailStore.selectedEntity) {
        return null;
    }

    return <div>
        <div>BarChart</div>
        <div>Structure Image</div>
        <div>Synonyms</div>
        <div>Identifier</div>
    </div>
}
export default observer(EntityDetailComponent)