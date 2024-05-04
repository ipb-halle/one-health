import { MultiSelect, MultiSelectChangeEvent } from "primereact/multiselect";
import { useState, useEffect, useContext } from "react";
import { IKeyword } from "./keyword";
import { dependencyFactory } from "../../injection/inversify.config";
import { IKeywordService } from "../../services/keyword-service";
import { SERVICE_TYPES } from "../../services";
import { RefObject } from "react";
import { Toast } from "primereact/toast";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { filter } from "rxjs";
import { MessageServiceContext } from "../../messages";
const React = require('react');

interface KeywordSearchProps {
    value: IKeyword[];
    valueSetter: (e : MultiSelectChangeEvent ) => void;
}


const KeywordSearch: React.FC<KeywordSearchProps> = ({value, valueSetter}) => {
    const keywordService = dependencyFactory.get<IKeywordService>(SERVICE_TYPES.IKeywordService);
    const {messageService} = useContext(MessageServiceContext);

    const [keywordOptions, setKeywordOptions] = useState<IKeyword[]>([]);
    const [availableOptions, setAvailableOptions] = useState<IKeyword[]>([]);
    const [filterValue, setFilterValue] = useState<string>("");
    
    useEffect(() => {
        const init = async () => {
            return await keywordService.getAll(messageService!).then(result => {
                setKeywordOptions(result);
            });
        }

        init();
    }, []);

    useEffect(() => {
        if (!filterValue){
            setAvailableOptions([...keywordOptions])
        } else {
            setAvailableOptions(keywordOptions.filter(x => x.value.toLowerCase().includes(filterValue.toLowerCase())))
        }

    }, [keywordOptions]);


    const handleAddKeyword = async () => {
        if (keywordOptions.find(x => x.value === filterValue))
            return;
        const newKeyword = await keywordService.create({value: filterValue}, messageService!);
        setKeywordOptions([...keywordOptions, newKeyword]);
    }


    const getFilterTemplate = () => {
        return <div className="p-inputgroup flex-1">

        <InputText 
            value={filterValue}
            placeholder="Search for keywords"
            onChange={(e) => {
                setFilterValue(e.target.value); // Update the value

                // Filter the dropdown options
                if (!e.target.value) {
                    setAvailableOptions(keywordOptions);
                } else {
                    setAvailableOptions(keywordOptions.filter(x => x.value.toLowerCase().includes(e.target.value.toLowerCase())));
                }

            }}
            style={{width: "100%"}}
        />

        <Button icon="pi pi-plus" 
            disabled={!filterValue} 
            onClick={handleAddKeyword}
        />
        </div>
        
    }
    
    return <MultiSelect 
            style={{ width: '100%' }}
            value={value}
            filter
            onChange={valueSetter}
            options={availableOptions}
            filterTemplate={getFilterTemplate}
            useOptionAsValue={true}
            optionLabel="value"
            optionValue="id"
            placeholder="Select keywords"
            maxSelectedLabels={10}
            className="w-full md:w-20rem"
    />
};

export default KeywordSearch;
