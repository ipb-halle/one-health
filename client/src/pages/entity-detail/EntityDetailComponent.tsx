import { RootStoreContext } from "@/app/providers/store-provider";
import { EntityDetailStore } from "@/store/EntityDetailStore";
import { observer } from "mobx-react-lite"
import { getSnapshot } from "mobx-state-tree";
import { IMSTMap, number } from "mobx-state-tree/dist/internal";
import { Chart } from "primereact/chart";
import { useContext } from "react"

function EntityDetailComponent() {
    const entityDetailStore = useContext(RootStoreContext).entityDetailStore;
    console.log("data: " + entityDetailStore.adjacentEntities.length);
    if (!entityDetailStore.selectedEntity) {
        return null;
    }

    return <div className="entity-detail">
        <div className="chart-box"><Chart type="bar"
            data={convertToChart(entityDetailStore.typeCounts)} 
            options={{ }}/></div>
        <div>Structure Image</div>
        <div>Synonyms</div>
        <div>Identifier</div>
    </div>
}
export default observer(EntityDetailComponent)

function convertToChart(typeCounts:IMSTMap<number>): any {
    let labels:string[] = [];
    let data:number[] = [];
    if (typeCounts) {
       typeCounts.forEach((value:number, key:string) => {
            labels.push(key); 
            data.push(value)}
        );
    }

    return { 
        labels : labels,
        datasets: [
            {
                label: "Related Entities",
                data: data,
                maxBarThickness: 40
            }
        ]
    };
}