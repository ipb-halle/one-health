import { RootStoreContext } from "@/app/providers/store-provider";
import SearchResultsContainerComponent from "@/features/search/general-search/components/search-results-container.component";
import GeneralSearchInput from "@/shared/components/GeneralSearchInput";
import { observer } from "mobx-react-lite";
import { useContext } from "react";

function MobileHomePageComponent() {

    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    const resultsPanel = (generalSearchStore.isSearching === null) ?
        <div>Earth Image</div> :
        <SearchResultsContainerComponent />

    return <div>
            <div>Title text</div>
            <GeneralSearchInput />
            {resultsPanel}
            <div>stats</div>
    </div>
}

export default observer(MobileHomePageComponent)