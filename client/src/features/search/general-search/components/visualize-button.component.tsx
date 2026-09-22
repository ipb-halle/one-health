import { Button } from 'primereact/button';
import { useContext } from 'react';
import { RootStoreContext } from '../../../../app/providers/store-provider';
import { useNavigate } from 'react-router-dom';
import './general-search.component.scss';

const VisualizeButton: React.FC = () => {
    const navigate = useNavigate();

    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;
    const neighborhoodExplorerStore =
        useContext(RootStoreContext).neighborhoodExplorerStore;

    return (
        <Button
            icon="fa fa-compass"
            className="visualize-button"
            label="Visualize"
            size="small"
            onClick={() => {
                neighborhoodExplorerStore.addNodes(
                    generalSearchStore.selectedEntities.map((x) => ({
                        data: {
                            id: x.id,
                            color: x.color,
                            label: x.name,
                        },
                    })),
                );

                navigate('/neighborhood-explorer');
            }}
            tooltip="Show selected records in neighborhood explorer"
            tooltipOptions={{
                position: 'bottom',
                showDelay: 1000,
            }}
        />
    );
};
export default VisualizeButton;
