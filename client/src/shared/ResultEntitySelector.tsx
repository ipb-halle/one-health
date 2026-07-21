import { RootStoreContext } from "@/app/providers/store-provider";
import { observer } from "mobx-react-lite";
import { Dropdown } from "primereact/dropdown";
import { useContext } from "react";

function convertTypeToOption(type: string, options: {type:string}[] ) {
    return options.find(o => {return o.type == type});
}

function ResultEntitySelector() {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    const options:{label:string, type:string}[] = [];
    generalSearchStore.typeCounts.forEach((value, key) => options.push(
        {label:key + " (" + value + ")", type: key as string}));


    return  <Dropdown value={convertTypeToOption(generalSearchStore.selectedType, options)} 
    onChange={(e) => { generalSearchStore.setSelectedType(e.value.type)}}
    options={options}
    />
}

export default observer (ResultEntitySelector)