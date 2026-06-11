import { TabView, TabPanel } from 'primereact/tabview';
import NaturalProductTable from './result-tables/natural-product-table.component';
import { useContext, useState } from 'react';
import PlantTable from './result-tables/plant-table.component';
import DiseaseTable from './result-tables/disease-table.component';
import { RootStoreContext } from '../../../../app/providers/store-provider';
import { observer } from 'mobx-react-lite';
import VisualizeButton from './visualize-button.component';

const SearchResultsPanel: React.FC = () => {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    const naturalProducts =
        generalSearchStore.getEntitiesOfType('Natural Product');
    const plants = generalSearchStore.getEntitiesOfType('Plant');
    const diseases = generalSearchStore.getEntitiesOfType('Disease');

    const maxResultsIndex = [
        naturalProducts.length,
        plants.length,
        diseases.length,
    ].reduce(
        (maxValueIndex, currentValue, currentIndex, arr) =>
            currentValue > arr[maxValueIndex] ? currentIndex : maxValueIndex,
        0,
    );

    const [activeIndex, setActiveIndex] = useState<number>(maxResultsIndex);

    return (
        <TabView
            pt={{ panelContainer: { style: { padding: '0rem' } } }}
            style={{ position: 'relative' }}
            renderActiveOnly={false}
            activeIndex={activeIndex}
            onTabChange={(e) => setActiveIndex(e.index)}>
            <TabPanel
                header={'Natural Products (' + naturalProducts.length + ')'}>
                <NaturalProductTable results={naturalProducts} />
                <VisualizeButton />
            </TabPanel>

            <TabPanel header={'Plants (' + plants.length + ')'}>
                <PlantTable results={plants} />
                <VisualizeButton />
            </TabPanel>

            <TabPanel header={'Diseases (' + diseases.length + ')'}>
                <DiseaseTable results={diseases} />
                <VisualizeButton />
            </TabPanel>
        </TabView>
    );
};

export default observer(SearchResultsPanel);
