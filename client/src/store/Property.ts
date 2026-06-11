import { types } from 'mobx-state-tree';

enum DataType {
    NUMBER = 'NUMBER',
    STRING = 'STRING',
    DATE = 'DATE',
    BOOLEAN = 'BOOLEAN',
}

export const Property = types.model({
    name: types.string,
    value: types.maybeNull(types.string),
    dataType: types.enumeration<DataType>(Object.values(DataType)),
    position: types.number,
});
