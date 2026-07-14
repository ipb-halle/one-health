import { RootStoreContext } from "@/app/providers/store-provider";
import { observer } from "mobx-react-lite";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import { useContext } from "react";

function GeneralSearchInput() {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    return <div className="p-inputgroup general-search-header-input">

        <InputText
            style={{
                border: 'none',
                boxShadow: 'none',
            }}
            value={generalSearchStore.query}
            onChange={(e) => {
                generalSearchStore.setQuery(e.target.value);
            }}
            onKeyDown={(e) => {
                if (e.key === 'Enter')
                    generalSearchStore.runQuery();
            }}
            placeholder="Search in knowledge base (e.g. disease name, plant name, compound name, InChI key, ...)"
        />
        <Button
            icon="pi pi-search"
            className="p-button-rounded p-button-text"
            onClick={() => generalSearchStore.runQuery()}
            tooltip="Search in knowledge base"
            tooltipOptions={{
                position: 'bottom',
                showDelay: 1000,
            }}
        />
    </div>
}

export default observer(GeneralSearchInput)