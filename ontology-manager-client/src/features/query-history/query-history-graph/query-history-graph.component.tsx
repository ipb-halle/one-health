
import { Component, RefObject, useReducer, useState } from "react";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import { Column } from "primereact/column";
import { IQueryHistoryGraphService } from "./query-history-graph.service";
import { dependencyFactory } from "../../shared/injection";
import { SERVICES } from "../../../services";


const React = require('react');

class QueryHistoryGraph extends Component {
    // const [filters, setFilters] = useReducer()
    queryHistoryGraphService: IQueryHistoryGraphService; 

    
    constructor(props:any){
        super(props);

        this.queryHistoryGraphService = dependencyFactory.get<IQueryHistoryGraphService>(SERVICES.IEntityTypeService);

    }


    render() {

    return <div>
           
        </div>
    }
};

