import { RootStoreContext } from "@/app/providers/store-provider";
import { LoadingPlaceholderComponent } from "@/shared/components";
import { observer } from "mobx-react-lite";
import { useContext } from "react";

function CompactResultDisplay() {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;


    return <div id="search-table">
        {generalSearchStore.isSearching && (
            <LoadingPlaceholderComponent />
        )}
        {generalSearchStore.isSearching === false && (
            (generalSearchStore.selectedType != "") ?
            <div>
                <div>{generalSearchStore.selectedType}</div>
                <div>Result Menu</div>
                <div>Entities</div>
            </div> :
            <div>NO RESULTS</div>
        )}
    </div>

}

export default observer(CompactResultDisplay)