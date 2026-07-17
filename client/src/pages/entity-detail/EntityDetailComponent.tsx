import { RootStoreContext } from "@/app/providers/store-provider";
import { observer } from "mobx-react-lite"
import { useContext } from "react"

function EntityDetailComponent() {
    const entityDetailStore = useContext(RootStoreContext).entityDetailStore;
    
    return <div>Entity Details</div>
}
export default observer(EntityDetailComponent)