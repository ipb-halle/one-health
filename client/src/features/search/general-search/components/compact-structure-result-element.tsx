import { CompactStructureEntity } from "@/core/types/compact-structure-entity"
import MolecularDrawComponent from "@/shared/components/molecular-draw.component"
import { Button } from "primereact/button"
import "./compact-result.scss"
import FormulaRenderer from "@/shared/components/FormulaRenderer"
import { useNavigate } from "react-router-dom"
import { useContext } from "react"
import { RootStoreContext } from "@/app/providers/store-provider"

function CompactStructureResultElement(data: { entity: CompactStructureEntity }) {
    
    const entityDetailsStore = useContext(RootStoreContext).entityDetailStore;
    const searchStoreStore = useContext(RootStoreContext).generalSearchStore;

    const navigate = useNavigate();

    return <div className="compactDisplayElement">
        <div className="main">
            <div className="structure">
                <MolecularDrawComponent
                    smiles={data.entity.molProps.smiles}
                    xkey={data.entity.molProps.xkey} />
            </div>
            <Button className="button" icon="pi pi-chart-bar" 
                            onClick={() => {
                    const entity = searchStoreStore.getEntityById(data.entity.id);
                    if (entity) {
                        entityDetailsStore.setSelectedEntity(entity);
                    }
                    navigate("/detail");
                }}
            />
        </div>
        <div className="details"><FormulaRenderer formula={data.entity.formula} /></div>
    </div>

}

export default (CompactStructureResultElement)