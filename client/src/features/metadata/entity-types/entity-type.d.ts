import { IKeyword } from "../keywords";
import { IProperty } from "../properties";

export interface IEntityType {
    id?: string;
    name: string;
    pluralName: string;
    parent?: any;
    description: string;
    color: string;
    label?: IProperty;
    keywords: IKeyword[] = [];
    properties: IProperty[] = [];

    // pluralName: string;
}
