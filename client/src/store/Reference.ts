import { types } from 'mobx-state-tree';

export const Reference = types.model({
    source: types.string,
    externalId: types.string,
    sourceUrl: types.string,
    entityId: types.string,
});
