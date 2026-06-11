import { ITypeQuery } from './type-query';

export interface ICoOcurrenceQuery {
    leftTypeQuery: ITypeQuery;
    rightTypeQuery: ITypeQuery;
}
