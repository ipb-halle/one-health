import { RootStoreContext } from "@/app/providers/store-provider";
import SearchResultsPanel from './search-results-table.component';
import { LoadingPlaceholderComponent } from "@/shared/components";

import { observer } from "mobx-react-lite";
import { useContext } from "react";

function SearchResultsContainerComponent() {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    return <div id="search-table">
        {generalSearchStore.isSearching && (
            <LoadingPlaceholderComponent />
        )}
        {generalSearchStore.isSearching === false && (
            <div className="general-search-table">
                <SearchResultsPanel />
            </div>
        )}{' '}
    </div>

}
export default observer(SearchResultsContainerComponent)