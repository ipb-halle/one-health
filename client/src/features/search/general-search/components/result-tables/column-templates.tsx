import MolecularDrawComponent from '../../../../../shared/components/molecular-draw.component';
import { truncateString } from '../../../../../shared';
import { IEntityDTO } from '../../models/entity-dto';
import { getPropertyValue } from '../../../../../shared/utils/get-property-value';

type IColumnTemplate = (rowData: IEntityDTO, options: any) => JSX.Element;

export const structureDrawTemplate: IColumnTemplate = (compound, options) => {
    return (
        <MolecularDrawComponent
            smiles={getPropertyValue(compound, 'SMILES')}
            xkey={options.rowIndex}
        />
    );
};

export const nameColumnTemplate: IColumnTemplate = (result) => {
    return <span>{truncateString(result.name, 150)}</span>;
};

export const weightColumnTemplate: IColumnTemplate = (compound) => {
    return (
        <>
            {Math.round(
                (getPropertyValue(compound, 'Molecular Weight') * 100) / 100,
            )}
        </>
    );
};

// TODO: find more generic way for columns calling getPropertyValue()

export const smilesColumnTemplate: IColumnTemplate = (compound) => {
    return getPropertyValue(compound, 'SMILES');
};

export const kingdomColumnTemplate: IColumnTemplate = (plant) => {
    return getPropertyValue(plant, 'Kingdom');
};

export const phylumColumnTemplate: IColumnTemplate = (plant) => {
    return getPropertyValue(plant, 'Phylum');
};

export const familyColumnTemplate: IColumnTemplate = (plant) => {
    return getPropertyValue(plant, 'Family');
};

export const classificationColumnTemplate: IColumnTemplate = (disease) => {
    return getPropertyValue(disease, 'Classification');
};
