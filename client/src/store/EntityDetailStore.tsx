import { clone, flow, getRoot, Instance, types } from 'mobx-state-tree';
import { Entity } from './Entity';
import { IEntityDTO } from '@/features/search/general-search/models/entity-dto';
import { EntityREST2MST } from './adapter/EntityREST2MST';
import { GET_ADJACENT_ENTITIES } from './adapter/REST_ENDPOINTS';
import { httpRequest } from '@/core/api/http/http-client';
import { RootStore } from './root-store';

export const EntityDetailStore = types
    .model({
        selectedEntity: types.maybeNull(types.safeReference(Entity)),
        adjacentEntities: types.array(Entity),
        typeCounts: types.map(types.number),
        isLoading: false,
    })
    .actions((self) => ({
        calculateTypeCounts(): void {
            self.typeCounts.clear();
            self.adjacentEntities.forEach((e) => {
                const oldCount = self.typeCounts.get(e.type) || 0;
                self.typeCounts.set(e.type, oldCount + 1);
            });
        },
    }))
    .actions((self) => ({
        loadAdjacentEntitiesOfSelection: flow(function* (): any {
            if (self.selectedEntity) {
                self.isLoading = true;
                const params = new URLSearchParams();
                params.append('id', self.selectedEntity.id);

                try {
                    const body: IEntityDTO[] = yield httpRequest<IEntityDTO[]>(
                        `${GET_ADJACENT_ENTITIES}?${params.toString()}`,
                    );

                    self.adjacentEntities.replace(EntityREST2MST(body));
                    self.calculateTypeCounts();
                } catch (e) {
                    //
                } finally {
                    self.isLoading = false;
                }
            }
        }),
    }))
    .actions((self) => ({
        setSelectedEntity(entity: Instance<typeof Entity>) {
            self.selectedEntity = entity;
            self.loadAdjacentEntitiesOfSelection();
        },
        investigateAdjacentEntries(): void {
            const entities: Instance<typeof Entity>[] = [];
            self.adjacentEntities.forEach((e) => entities.push(clone(e)));
            self.adjacentEntities.replace([]);
            const generalSearchStore =
                getRoot<typeof RootStore>(self).generalSearchStore;
            generalSearchStore.setResults(entities);
            generalSearchStore.setQuery('');
        },
    }));
