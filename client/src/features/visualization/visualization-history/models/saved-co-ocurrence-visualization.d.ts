import { ICoOcurrenceQuery } from './co-ocurrence-query';

export interface ISavedCoOcurrenceVisualization {
    id: string;
    name: string;
    visualization: string;
    query: ICoOcurrenceQuery;
}
