import { types } from 'mobx-state-tree';
import { Property } from './Property';
import { Reference } from './Reference';

export const Entity = types.model({
    id: types.identifier,
    type: types.string,
    labels: types.array(types.string),
    properties: types.array(Property),
    references: types.maybeNull(types.array(Reference)),
    synonyms: types.array(types.string),
    color: types.string,
    name: types.string,
});
