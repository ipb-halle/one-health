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
            src={earthImage} />
    );

    const resultsPanel = (generalSearchStore.isSearching === null) ?
        <Card
            title="Investigate"
            footer={' '}
            header={earthCardHeader}
            className="md:w-25rem card">
            <p>
                Uncover previously unknown relationships.
            </p>
            <p style={{ fontSize: '16px', color: '#a40', margin: 0 }}>
                This service is <strong>work in progress</strong>, layout and
                function are subject to change. Works best with a desktop browser.
            </p>
        </Card> :
        <CompactResultDisplay />

    return <div className="mobile">
        <div className="search">
            <div className="title">General Search</div>
            <div className="input"><GeneralSearchInput /></div>
        </div>
        {resultsPanel}
    </div>
}

export default observer(MobileHomePageComponent)