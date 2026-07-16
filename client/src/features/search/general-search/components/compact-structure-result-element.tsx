import { CompactStructureEntity } from "@/core/types/compact-structure-entity"
import MolecularDrawComponent from "@/shared/components/molecular-draw.component"
import { Button } from "primereact/button"

function CompactStructureResultElement(data: { entity: CompactStructureEntity }) {

    return <div>
        <div>
            <MolecularDrawComponent
                smiles={data.entity.molProps.smiles}
                xkey={data.entity.molProps.xkey} />
            <Button label="X" />
        </div>
        <div>{data.entity.formula}</div>
    </div>

}

export default (CompactStructureResultElement)