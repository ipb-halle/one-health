import { types } from 'mobx-state-tree';

export const SavedTextSearch = types.model({
    id: types.string,
    datetime: types.maybeNull(types.string),
    query: types.string,
    results: types.maybeNull(types.array(types.string)),
});
