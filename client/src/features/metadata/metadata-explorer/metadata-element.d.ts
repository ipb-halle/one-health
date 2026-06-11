import { IProperty } from '../properties';

export interface IMetadataElement {
    id: string;
    name: string;
    description: string;
    type: string;
    count: number;
    properties: IProperty[];
}
