import { InputText } from "primereact/inputtext";
import { useContext } from "react";
import { RootStoreContext } from "@/app/providers/store-provider";
import { observer } from "mobx-react-lite";

function SearchBar() {

    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    const handleKeyDown =
        (e: React.KeyboardEvent<HTMLInputElement>) => {
            if (e.key === "Enter") {
                generalSearchStore.runQuery();
            }
        };

    return (
        <div className="mobile-search-bar-section">
            <div className="mobile-search-input-wrapper">
                <InputText
                    className="mobile-search-input"
                    value={generalSearchStore.query}
                    onChange={(e) => generalSearchStore.setQuery(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="Search in knowledge base (e.g. disease name, ...)"
                />
                <button
                    type="button"
                    className="mobile-search-btn"
                    onClick={generalSearchStore.runQuery}
                    aria-label="Search"
                    title="Search"
                >
                    <i className="pi pi-search" />
                </button>
            </div>
        </div>
    );
}

export default observer(SearchBar);