import { RootStoreContext } from "@/app/providers/store-provider";
import { observer } from "mobx-react-lite"
import { IMSTMap, number } from "mobx-state-tree/dist/internal";
import { Chart } from "primereact/chart";
import { useContext } from "react"

function EntityDetailComponent() {
    const entityDetailStore = useContext(RootStoreContext).entityDetailStore;
    if (!entityDetailStore.selectedEntity) {
        return null;
    }

    return <div className="entity-detail">
        <div className="chart-box"><Chart type="bar"
            data={convertToChart(entityDetailStore.typeCounts)}
            options={{}} /></div>
        <div>Structure Image</div>
        <div>Synonyms</div>
        <div>Identifier</div>
    </div>
}
export default observer(EntityDetailComponent)

function convertToChart(typeCounts: IMSTMap<typeof number>): any {
    let labels: string[] = [];
    let data: number[] = [];
    if (typeCounts) {
        typeCounts.forEach((value: number, key: string | number) => {
            labels.push(key.toString());
            data.push(value)
        }
        );
    }

    return {
        labels: labels,
        datasets: [
            {
                label: "Related Entities",
                data: data,
                maxBarThickness: 40
            }
        ]
    };
}