import { flow, Instance, types } from "mobx-state-tree";
import { Entity } from "./Entity";

export const EntityDetailStore = types
    .model({
        selectedEntity: types.maybeNull(types.safeReference(Entity)),
        adjacentEntities: types.array(Entity)
    })
    .actions((self) => ({
        loadAdjacentEntitiesOfSelection: flow(function* (): any {
            if (self.selectedEntity) {
                const params = new URLSearchParams();
                params.append("id", self.selectedEntity?.id);
                const response = yield fetch(
                    "api/entity/getAdjacentEntities?" + params.toString()
                )
            }
        })
    }))

    .actions((self) => ({
        setSelectedEntity(entity: Instance<typeof Entity>) {
            self.selectedEntity = entity;
            self.loadAdjacentEntitiesOfSelection();
        },

    }));

