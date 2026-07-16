import { CompactTextEntity } from "@/core/types/compact-text-entity"
import { Button } from "primereact/button"

function CompactTextResultElement(data: {entity: CompactTextEntity}) {

    return <div>
        <div>
            <div>{data.entity.name}</div>
            <Button label="!!!" /></div>
        <div>{data.entity.details}</div>
    </div>
}

export default (CompactTextResultElement)