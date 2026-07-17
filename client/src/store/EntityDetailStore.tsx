import { flow, Instance, types } from "mobx-state-tree";
import { Entity } from "./Entity";
import { IEntityDTO } from "@/features/search/general-search/models/entity-dto";
import { EntityREST2MST } from "./adapter/EntityREST2MST";

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
                );
                if (response.status == 200) {
                    const body = yield response.json() as IEntityDTO[];
                    self.adjacentEntities.replace(EntityREST2MST(body));
                } else {
                    // ToDo: provide error handling
                }
            }
        })
    }))

    .actions((self) => ({
        setSelectedEntity(entity: Instance<typeof Entity>) {
            self.selectedEntity = entity;
            self.loadAdjacentEntitiesOfSelection();
        },

    }));

