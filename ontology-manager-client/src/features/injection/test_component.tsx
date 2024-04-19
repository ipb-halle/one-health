import { IDataSourceService, ILinkTypeService, IPropertyService } from "../services";
import { IEntityTypeService } from "../services/entity-type-service";
import { SERVICE_TYPES } from "../services/service-types";
import { dependencyFactory } from "./inversify.config";
import { MyInterface, MyStringInterface, Person } from "./test";
import { TYPES } from "./types";

const React = require('react');



const InjectionTest: React.FC = () => {
    const e = dependencyFactory.get<IEntityTypeService>(SERVICE_TYPES.IEntityTypeService);
    const l = dependencyFactory.get<ILinkTypeService>(SERVICE_TYPES.ILinkTypeService);
    const p = dependencyFactory.get<IPropertyService>(SERVICE_TYPES.IPropertyService);
    const d = dependencyFactory.get<IDataSourceService>(SERVICE_TYPES.IDataSourceService);


    return <></>;
};

export default InjectionTest;