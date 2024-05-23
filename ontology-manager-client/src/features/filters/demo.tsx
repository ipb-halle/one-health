
import { RefObject, useReducer, useState } from "react";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import { Column } from "primereact/column";
import { FilterInfo } from "./interfaces/filter-info";


const React = require('react');

const FiltersDemo: React.FC = () => {
    // const [filters, setFilters] = useReducer()

    const [filter, setValue] = useState<FilterInfo>({property: "some"});

    return <div>
            <div style={{width:  "300px"}}>

                {/* <ChemicalStructureFilter filterInfo={filter} onChange={setValue} ></ChemicalStructureFilter> */}
       
            </div>
        </div>
};

export default FiltersDemo;
