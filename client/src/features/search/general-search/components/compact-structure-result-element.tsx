import { CompactStructureEntity } from "@/core/types/compact-structure-entity"
import MolecularDrawComponent from "@/shared/components/molecular-draw.component"
import { Button } from "primereact/button"
import "./compact-result.scss"

function CompactStructureResultElement(data: { entity: CompactStructureEntity }) {

    return <div className="compactDisplayElement">
        <div className="main">
            <div className="structure">
                <MolecularDrawComponent
                    smiles={data.entity.molProps.smiles}
                    xkey={data.entity.molProps.xkey} />
            </div>
            <Button className="button" icon="pi pi-chart-bar" />
        </div>
        <div className="details">{data.entity.formula}</div>
    </div>

}

export default (CompactStructureResultElement)