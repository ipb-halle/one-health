import { RootStoreContext } from "@/app/providers/store-provider";
import { LoadingPlaceholderComponent } from "@/shared/components";
import { EntityDetailStore } from "@/store/EntityDetailStore";
import { observer } from "mobx-react-lite"
import { IMSTMap, Instance, number } from "mobx-state-tree/dist/internal";
import { Chart } from "primereact/chart";
import { useContext } from "react"
import { getPropertyValue } from "@/shared/utils/get-property-value";
import MolecularDrawComponent from "@/shared/components/molecular-draw.component";
import { useNavigate } from "react-router-dom"
import "./entityDetailComponent.scss";
import { Button } from "primereact/button";

function EntityDetailComponent() {
    const entityDetailStore = useContext(RootStoreContext).entityDetailStore;
    const navigate = useNavigate();

    if (!entityDetailStore.selectedEntity) {
        return null;
    }

    return <div className="entity-detail">
        <center><h1>Details</h1></center>
        {relationBarChart(entityDetailStore, navigate)}
        {navigationButtons(entityDetailStore, navigate)}
        {entityDetailStore.isLoading && (<LoadingPlaceholderComponent />)}
        {structureImage(entityDetailStore)}
        {synonyms(entityDetailStore)}
    </div>
}
export default observer(EntityDetailComponent)

function structureImage(entityDetailStore: Instance<typeof EntityDetailStore>): JSX.Element | null {

    if (entityDetailStore.selectedEntity?.type == "Natural Product") {
        const smiles = getPropertyValue(entityDetailStore.selectedEntity, "SMILES");

        console.log(smiles);
        return <div className="structure">
            <MolecularDrawComponent
                smiles={smiles || ""}
                xkey={entityDetailStore.selectedEntity.id} />
        </div>
    } 
    return null;
}
function synonyms(entityDetailStore: Instance<typeof EntityDetailStore>): JSX.Element | null  {
    let mockId = 0;
    return <div className="synonyms">
        <center><h2>Synonyms</h2></center>
        {entityDetailStore.selectedEntity?.synonyms.map((s) => { return <div className="synonymText" key={"s"+mockId++}>{s}</div> })}
        </div>;
}


function navigationButtons(entityDetailStore: Instance<typeof EntityDetailStore>, navigate: (target:string)=>void): JSX.Element {
    return <div className="buttons">
        <Button icon="pi pi-directions-alt" label="Return" onClick={()=>navigate("/")} />{" "}
        {(entityDetailStore.adjacentEntities.length > 0) && 
            <Button icon="pi pi-search" label="Investigate relations" onClick={()=>investigateAdjacentEntries(entityDetailStore, navigate)}/>}
    </div>    
}

function relationBarChart(entityDetailStore: Instance<typeof EntityDetailStore>, navigate: (target:string)=>void): JSX.Element | null {
    if (entityDetailStore.isLoading) {
        return null;
    }
    return (entityDetailStore.adjacentEntities.length > 0) ?
        <div className="chart-box"><Chart type="bar"
            data={convertToChart(entityDetailStore.typeCounts)}
            options={{}} />
        </div> :
        <div><b>No related entities found</b></div>
}

function investigateAdjacentEntries(entityDetailStore: Instance<typeof EntityDetailStore>, 
    navigate: (target:string)=>void): void {
    entityDetailStore.investigateAdjacentEntries();
    navigate("/");
}

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