import { IFilter } from './filter';

export interface ITypeQuery {
    type?: string;
    filters?: IFilter[];
    groupBy?: string;
}
