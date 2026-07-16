import { MolecularDrawComponentProps } from "./molecular-draw-component-props";

export interface CompactStructureEntity {
    id: string|number;
    molProps: MolecularDrawComponentProps;
    formula?: string;
}