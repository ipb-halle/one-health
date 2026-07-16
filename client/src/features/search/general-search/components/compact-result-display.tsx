import { RootStoreContext } from "@/app/providers/store-provider";
import { LoadingPlaceholderComponent } from "@/shared/components";
import ResultEntitySelector from "@/shared/ResultEntitySelector";
import { observer } from "mobx-react-lite";
import { useContext } from "react";
import CompactTextResultElement from "./compact-text-result-element";
import { Entity } from "@/store/Entity";
import { Property } from "@/store/Property";
import { Instance } from "mobx-state-tree";
import { CompactTextEntity } from "@/core/types/compact-text-entity";
import { CompactStructureEntity } from "@/core/types/compact-structure-entity";
import CompactStructureResultElement from "./compact-structure-result-element";

function CompactResultDisplay() {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;


    const components = createCompactComponents(
        generalSearchStore.getEntitiesOfType(generalSearchStore.selectedType) );

    return <div id="search-table">
        {generalSearchStore.isSearching && (
            <LoadingPlaceholderComponent />
        )}
        {generalSearchStore.isSearching === false && (
            (generalSearchStore.selectedType != "") ?
                <div>
                    <div>{generalSearchStore.selectedType}</div>
                    <ResultEntitySelector />
                    {components}
                </div> :
                <div>NO RESULTS</div>
        )}
    </div>

}

export default observer(CompactResultDisplay)

function mapNamedProperty(propName: string, prop: Instance<typeof Property>[]): string {
    let classification = "";
    prop.forEach((p) => { if (p.name === propName) { classification = p.value || ""; } });
    return classification;
}

function mapEntity(entity: Instance<typeof Entity>): CompactTextEntity | CompactStructureEntity {
    switch (entity.type) {
        case "Disease": return {
            id: entity.id,
            name: entity.name,
            details: mapNamedProperty("Classification", entity.properties)
        };
        case "Plant": return {
            id: entity.id,
            name: entity.name,
            details: mapNamedProperty("Family", entity.properties)
        };
        case "Natural Product":
            return {
                id: entity.id,
                molProps: { xkey: entity.id, smiles: mapNamedProperty("SMILES", entity.properties) },
                formula: mapNamedProperty("Molecular Formula", entity.properties)
            };
    }
    return { id: "", name: "", details: "Unknown record type" };
}


function createCompactComponents(entities: Instance<typeof Entity>[]):
    React.ReactElement<typeof CompactTextResultElement>[] |
    React.ReactElement<typeof CompactStructureResultElement>[] {
    if (entities.length == 0) { return [] };

    return entities.map(e => {
        if (e.type == "Natural Product") {
            return <CompactStructureResultElement 
                    entity={mapEntity(e) as CompactStructureEntity} key={e.id} />
        } else {
            return <CompactTextResultElement 
                    entity={mapEntity(e) as CompactTextEntity} key={e.id} />
        }
    })
}