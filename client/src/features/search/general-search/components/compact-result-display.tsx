import { RootStoreContext } from "@/app/providers/store-provider";
import { LoadingPlaceholderComponent } from "@/shared/components";
import ResultEntitySelector from "@/shared/ResultEntitySelector";
import { observer } from "mobx-react-lite";
import { useContext } from "react";
import CompactTextResultElement from "./compact-text-result-element";

function mapEntity(entity: { id: string, type: string, name: string }): { id: string, "name": string, "details": string } {
    return { id: "xx", name: "xx", details: "xx" };
}

function CompactResultDisplay() {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    const records = ((generalSearchStore.selectedType == "Disease")
        || (generalSearchStore.selectedType == "Plant")) ?
        generalSearchStore.entities.map((e) => {
            return <CompactTextResultElement
                entity={mapEntity(e)} />
        })
        : <div />

    return <div id="search-table">
        {generalSearchStore.isSearching && (
            <LoadingPlaceholderComponent />
        )}
        {generalSearchStore.isSearching === false && (
            (generalSearchStore.selectedType != "") ?
                <div>
                    <div>{generalSearchStore.selectedType}</div>
                    <ResultEntitySelector />
                    {records}
                </div> :
                <div>NO RESULTS</div>
        )}
    </div>

}

export default observer(CompactResultDisplay)