import { IKeyword } from '../keywords';
import { IProperty } from '../properties';

export interface ILinkType {
    id?: string;
    name: string;
    direction: string;
    leftEntityTypeId: string;
    leftCardinality: string;
    rightEntityTypeId: string;
    rightCardinality: string;
    description: string;
    keywords: IKeyword[];
    properties: IProperty[];
}
