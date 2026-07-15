import { RootStoreContext } from "@/app/providers/store-provider";
import CompactResultDisplay from "@/features/search/general-search/components/compact-result-display";
import GeneralSearchInput from "@/shared/components/GeneralSearchInput";
import { observer } from "mobx-react-lite";
import { Card } from "primereact/card";
import { useContext } from "react";
import earthImage from '../../assets/img/earth_image.png';
import './mobile-home-page.component.scss';

function MobileHomePageComponent() {

    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    const earthCardHeader = (
        <img
            alt="Earth surrounded by life and microbes"
            src={earthImage} 
            className="mobile-card-img"/>
    );

    const resultsPanel = (generalSearchStore.isSearching === null) ?
        <Card
            title="Investigate"
            footer={' '}
            header={earthCardHeader}
            className="md:w-25rem mobile-card">
            <p>
                Uncover previously unknown relationships.
            </p>
        </Card> :
        <CompactResultDisplay />

    return <div>
        <div>Title text</div>
        <GeneralSearchInput />
        {resultsPanel}
        <div>stats</div>
    </div>
}

export default observer(MobileHomePageComponent)