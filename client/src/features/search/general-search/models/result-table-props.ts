import { Dispatch, SetStateAction } from 'react';
import { IEntityDTO } from './entity-dto';

export interface ResultTableProps {
    results: IEntityDTO[];
    selectedElements: IEntityDTO[];
    setSelectedElements: Dispatch<SetStateAction<IEntityDTO[]>>;
}
