import { CompactTextEntity } from "@/core/types/compact-text-entity"
import { Button } from "primereact/button"
import "./compact-result.scss"
import { RootStoreContext } from "@/app/providers/store-provider"
import { useContext } from "react"
import { useNavigate } from "react-router-dom"

function CompactTextResultElement(data: { entity: CompactTextEntity }) {
    const entityDetailsStore = useContext(RootStoreContext).entityDetailStore;
    const searchStoreStore = useContext(RootStoreContext).generalSearchStore;
    const navigate = useNavigate();

    return <div className="compactDisplayElement">
        <div className="main">
            <div className={data.entity.styleClass}>{data.entity.name}</div>
            <Button
                className="button" icon="pi pi-chart-bar"
                onClick={() => {
                    const entity = searchStoreStore.getEntityById(data.entity.id);
                    if (entity) {
                        entityDetailsStore.setSelectedEntity(entity);
                    }
                    navigate("/detail");
                }}
            /></div>
        <div className="details">{data.entity.details}</div>
    </div>
}

export default (CompactTextResultElement)