import { Instance, types } from "mobx-state-tree";
import { Entity } from "./Entity";

export const EntityDetailStore = types
    .model({
        selectedEntity: types.maybeNull(types.safeReference(Entity))
    })

    .actions((self) => ({
        setSelectedEntity(entity: Instance<typeof Entity>) {
            self.selectedEntity = entity;
        }
    }));

