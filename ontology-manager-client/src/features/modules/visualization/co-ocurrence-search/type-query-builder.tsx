
import { Component, useContext, useState } from "react";
import { Column } from "primereact/column";
import { ITypeQuery } from "./type-query";
import { Dropdown } from "primereact/dropdown";

import { useEffect } from "react";
import { filter } from "rxjs";
import { IFilter } from "./filter";
import { InputText } from "primereact/inputtext";
import { ScrollPanel } from "primereact/scrollpanel";
import { dependencyFactory } from "../../../shared/injection";
import { IEntityTypeService, SERVICES } from "../../../../services";
import { MessageService, MessageServiceContext } from "../../../shared/messages";
import { SelectableOption } from "../../../../utils/selectable-option";
import { CollectionPlaceholderComponent } from "../../../../components";
import { FloatLabel } from 'primereact/floatlabel';

const React = require('react');

interface TypeQueryBuilderProps {
    messageService: MessageService;
    triggerQuery: boolean;
    parentUpdate: any;
    height: any;
    width: any;
}

interface TypeQueryBuilderState {
    query: ITypeQuery;
    groupByOptions: SelectableOption[];
}


class TypeQueryBuilder extends Component<TypeQueryBuilderProps, TypeQueryBuilderState> {

    entityService : IEntityTypeService;
    messageService : MessageService;
    options: SelectableOption[] = [];
    

    constructor(props: TypeQueryBuilderProps) {
        super(props);
        this.entityService = dependencyFactory.get<IEntityTypeService>(SERVICES.IEntityTypeService);
        this.messageService = props.messageService;


        this.state = {
            query: {} as ITypeQuery,
            groupByOptions: [] as SelectableOption[]
        };

        this.setQuery = this.setQuery.bind(this);
        this.setGroupByOptions = this.setGroupByOptions.bind(this);
        
    }

    setQuery(newQuery: ITypeQuery) {
        this.setState((prevState) => ({
            query: newQuery     
        }));
    }

    setGroupByOptions(newGroupByOptions: SelectableOption[]) {
        this.setState((prevState) => ({
            groupByOptions: newGroupByOptions      
        }));
    } 

    componentDidMount(): void {
        this.initWidgets();
    }



    async initWidgets() {
        this.options = await this.entityService.getAllEntityTypesAsOptions(this.messageService!);
    }
    


    async onTypeChangedHandler () {
        if (this.state.query.type){

           var  entityType = await this.entityService.get(this.state.query.type, this.messageService!)
           var filters = entityType.properties.map((x) => { return { property: x.name, value: undefined} });

           this.setGroupByOptions(entityType.properties.map((x) => { return { label: x.name, value: x.name};}));

           console.log(entityType.label!.name);
           this.setQuery({...this.state.query, groupBy: entityType.label!.name, filters: filters});
        }


    };


    getQuery() {
        return this.state.query;
    }

    async loadQuery(query: ITypeQuery) {
    
        if (query.type){
            
            var  entityType = await this.entityService.get(query.type!, this.messageService!)
            
            this.setGroupByOptions(entityType.properties.map((x) => { return { label: x.name, value: x.name};}));
        }
        
        this.setQuery({...query});
    }

    reset() {
        this.setQuery({});
        this.setGroupByOptions([]);
    }



    render () {
        
        return <div style={{border: "1px solid #DEE2E6", height:this.props.height, width: this.props.width, marginBottom: "7px", boxSizing: 'border-box'}}>

            <div style={{backgroundColor: "#F8F9FA", padding: "5px", borderBottom: "1px solid #DEE2E6"}}>
                <Dropdown 
                    style={{width: "100%"}} 
                    options={this.options} 
                    value={this.state.query.type}
                    onChange={async (e) => {
                        var  entityType = await this.entityService.get(e.value, this.messageService!)
                        var filters = entityType.properties.map((x) => { return { property: x.name, value: undefined} });
             
                        this.setGroupByOptions(entityType.properties.map((x) => { return { label: x.name, value: x.name};}));
             
                        this.setQuery({...this.state.query, groupBy: entityType.label!.name, filters: filters, type:e.value});
                    }}
                    filter

                />
            </div>
           
            <ScrollPanel style={{padding: 2, height: this.props.height - 53}}>
            
            {
                !this.state.query.type && 
                <CollectionPlaceholderComponent icon="pi pi-filter" message=""></CollectionPlaceholderComponent>
            }
            
            {
                this.state.query.type && <>

                <div style={{padding: "5px", borderBottom: "1px solid #DEE2E6"}}>
                    <label style={{minWidth:"122px"}}>Dimension</label>
                    <Dropdown  
                        style={{width: "100%"}}
                        value={this.state.query.groupBy} 
                        options={this.state.groupByOptions} 
                        onChange={(e) => {
                            this.setQuery({...this.state.query, groupBy: e.target.value}

                        )}}
                        filter
                    /> 

                </div>

                {this.state.query.filters?.map((x:IFilter, i:number)=> {
                    return <div style={{padding:"5px", borderBottom: "1px solid #DEE2E6"}}>
                        {
                        this.state.query.filters &&
                        <div>

                            <small>{x.property}</small>
                            <div></div>
                            <InputText 
                                style={{width: "100%"}}
                                className="p-inputtext-sm"
                                value={this.state.query.filters[i].value} 
                                onChange={(e) => {
                                    this.state.query.filters![i].value = e.target.value;
                                    this.setQuery({...this.state.query});
                                }}/>
                        </div>
                            

                        }
                    </div> 
                    
                })}
                </>

            }

            </ScrollPanel>

        
        </div>
    }

};

export default TypeQueryBuilder;
