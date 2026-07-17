import { Instance, types } from "mobx-state-tree";
import { Entity } from "./Entity";

export const EntityDetailStore = types
    .model({
        selectEntity: types.maybeNull(types.reference(Entity))
    })

    .actions((self) => ({
        setSelectedEntity(entity: Instance<typeof Entity>) {
            self.selectEntity = entity;
        }

    }));

