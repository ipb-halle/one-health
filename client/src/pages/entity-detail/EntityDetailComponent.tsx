import { RootStoreContext } from "@/app/providers/store-provider";
import { EntityDetailStore } from "@/store/EntityDetailStore";
import { observer } from "mobx-react-lite"
import { useContext } from "react"

function EntityDetailComponent() {
    const entityDetailStore = useContext(RootStoreContext).entityDetailStore;
    console.log("data: " + entityDetailStore.adjacentEntities.length);
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