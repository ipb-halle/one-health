import { Button } from "primereact/button"

function CompactTextResultElement(data: {entity: { id: string, name: string, details: string }}) {

    return <div>
        <div>
            <div>{data.entity.name}</div>
            <Button label="!!!" /></div>
        <div>{data.entity.details}</div>
    </div>
}

export default (CompactTextResultElement)