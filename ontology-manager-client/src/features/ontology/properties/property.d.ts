import { DataTypes } from '../data-types/data-types';

export interface IProperty {
    id?: string;
    name: string;
    description: string;
    key: boolean;
    dataType: string; 
    inherited: boolean;
    
}
