import { dependencyFactory } from "../../injection/inversify.config";
import { ILinkTypeService } from "../../services";
import { SERVICE_TYPES } from "../../services";
import { RefObject, useState } from "react";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import { Column } from "primereact/column";
import LazyLoadGrid from "../../utils/grid/grid.component";
import { ITypeQuery } from "./type-query";
import { Dropdown } from "primereact/dropdown";
import { IEntityTypeService } from "../../services";
import { SelectableOption } from "../../utils/selectable-option";
import { useEffect } from "react";
import { filter } from "rxjs";
import { IFilter } from "./filter";
import { InputText } from "primereact/inputtext";
import { ScrollPanel } from "primereact/scrollpanel";


const React = require('react');

interface TypeQueryBuilderProps {
    triggerQuery: boolean;
    parentUpdate: any;
    toast: RefObject<Toast>;
}


const TypeQueryBuilder: React.FC<TypeQueryBuilderProps> = ({toast, triggerQuery, parentUpdate}) => {
    const entityService = dependencyFactory.get<IEntityTypeService>(SERVICE_TYPES.IEntityTypeService);
    const [query, setQuery] = useState<ITypeQuery>({});
    const [options, setOptions] = useState<SelectableOption[]>([]);
    const [groupByOptions, setGroupByOptions] = useState<SelectableOption[]>([]);


    useEffect(
        () => {
            init();
        }, []
    )

    const init = async () => {
        setOptions(
            await entityService.getAllEntityTypesAsOptions(toast)
        );

    }

    useEffect(
        () => {
            parentUpdate(query);
        }, [triggerQuery]
    )


    useEffect(() => {
        onTypeChangedHandler();
    }, [query.type])

    const onTypeChangedHandler = async () => {
        if (query.type){

           var  entityType = await entityService.get(query.type, toast)
           var filters = entityType.properties.map((x) => { return { property: x.name, value: undefined} });

           setGroupByOptions(entityType.properties.map((x) => { return { label: x.name, value: x.name};}));

           setQuery({...query, groupBy: undefined, filters: filters});
        }


    };


    return <div style={{border: "1px solid #DEE2E6", height:"49.5%", marginBottom: "7px"}}>

        <div style={{backgroundColor: "#F8F9FA", padding: "5px", borderBottom: "1px solid #DEE2E6"}}>
            <Dropdown 
                style={{width: "100%"}} 
                options={options} 
                value={query.type}
                onChange={(e) => {setQuery({...query, type: e.value});}}
                filter

            />
        </div>
        {
            query.type &&
        <ScrollPanel style={{height: "292px"}}>

            <div style={{padding: "5px", borderBottom: "1px solid #DEE2E6"}}>
                <label style={{minWidth:"122px"}}>Dimension</label>
                <Dropdown  
                    style={{width: "58%"}}
                    value={query.groupBy} 
                    options={groupByOptions} 
                    onChange={(e) => {setQuery({...query, groupBy: e.target.value});}}
                    filter
                /> 

            </div>

            {query.filters?.map((x:IFilter, i:number)=> {
                return <div style={{padding:"5px", borderBottom: "1px solid #DEE2E6"}}>
                    {
                    query.filters &&
                    <span>
                        <label style={{minWidth:"122px"}}>{x.property}</label>
                        <InputText 
                            className="p-inputtext-sm"
                            value={query.filters[i].value} 
                            onChange={(e) => {
                                query.filters![i].value = e.target.value;
                                setQuery({...query});
                                console.log(query);
                            }}/>
                         
                    </span>
                    }
                </div> 
                
            })}
            
        </ScrollPanel>
        }

       
    </div>

};

export default TypeQueryBuilder;
