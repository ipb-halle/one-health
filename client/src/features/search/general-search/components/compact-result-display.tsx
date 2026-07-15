import { RootStoreContext } from "@/app/providers/store-provider";
import { LoadingPlaceholderComponent } from "@/shared/components";
import ResultEntitySelector from "@/shared/ResultEntitySelector";
import { observer } from "mobx-react-lite";
import { useContext } from "react";
import CompactTextResultElement from "./compact-text-result-element";

function mapNamedProperty(propName: string, prop:{name:string, value:string|null}[]): string {
    let classification = "";
    prop.forEach((p) => {if (p.name === propName) { classification = p.value || "";} });
    return classification;
}

function mapEntity(entity: { id: string, type: string, name: string, 
    properties:{name:string, value:string|null}[]}): { id: string, "name": string, "details": string } {
    switch(entity.type) {
        case "Disease" : return {id: entity.id, name: entity.name, details: mapNamedProperty("Classification", entity.properties)};
        case "Plant" : return {id: entity.id, name: entity.name, details: mapNamedProperty("Family", entity.properties)};
        case "Natural Product" : return { id: entity.id, name: "xx", details: "xx" };
    }
    return { id: "", name: "", details: "Unknown record type" };
}

function CompactResultDisplay() {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    const records = ((generalSearchStore.selectedType == "Disease")
        || (generalSearchStore.selectedType == "Plant")) ?
        generalSearchStore.getEntitiesOfType(generalSearchStore.selectedType).map((e) => {
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