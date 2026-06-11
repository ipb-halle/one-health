import { IProperty } from './property';

export interface IEntityDTO {
    id: string;
    type: string;
    labels: string[];
    properties: IProperty[];
    references?: any;
    synonyms: string[];
    color: string;
    name: string;
}
