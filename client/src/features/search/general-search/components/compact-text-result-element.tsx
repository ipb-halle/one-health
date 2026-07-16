import { CompactTextEntity } from "@/core/types/compact-text-entity"
import { Button } from "primereact/button"
import "./compact-result.scss"

function CompactTextResultElement(data: {entity: CompactTextEntity}) {

    return <div className="compactDisplayElement">
        <div className="main">
            <div className={data.entity.styleClass}>{data.entity.name}</div>
            <Button className="button" icon="pi pi-chart-bar" /></div>
        <div className="details">{data.entity.details}</div>
    </div>
}

export default (CompactTextResultElement)