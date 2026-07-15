import { types, flow, getEnv, getSnapshot, getRoot, Instance } from 'mobx-state-tree';
import { Entity } from '../../../store/Entity';
import React from 'react';
import { IGeneralSearchHistoryService } from '../search-history/services/general-search-history-service';
import { IGeneralSearchService } from './services/general-search-service';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

import { RootStore } from '../../../store/root-store';
type Env = {
    historyService: IGeneralSearchHistoryService;
    messageService: MessageService;
    searchService: IGeneralSearchService;
};

export const GeneralSearchStore = types
    .model({
        entities: types.array(Entity), // optional?
        typeCounts: types.map(types.number),
        selectedType: "",
        selectedEntities: types.array(types.reference(Entity)),
        isSearching: types.maybeNull(types.boolean),
        query: types.optional(types.string, ''),

    })
    .views((self) => ({
        getMajorityType(): string {
            let currentMax = 0;
            let maxKey: string = "";
            self.typeCounts.forEach((value, key) => {
                if (value > currentMax) {
                    maxKey = key as string;
                    currentMax = value;
                }
            });
            return maxKey;
        },
        getEntitiesAsJSON() {
            return self.entities.map((entity) => getSnapshot(entity));
        },
        getSelectionAsJSON(): any {
            return self.selectedEntities.map((entity) => getSnapshot(entity));
        },
        getEntitiesOfType(type: string) {
            return self.entities.filter((e) => e.type == type);
        },
    }))
    .actions((self) => ({
        calculateTypeCounts(): void {
            self.typeCounts.clear();
            self.entities.map(e => {
                const oldCount = self.typeCounts.get(e.type) || 0;
                self.typeCounts.set(e.type, oldCount + 1);
                return 0;
            });
        }
    }))
    .actions((self) => ({
        setIsSearching(isSearching: boolean): void {
            self.isSearching = isSearching;
        },
        setQuery(query: string): void {
            self.query = query;
        },
        setSelectedEntities(entities: any[]) {
            self.selectedEntities.clear();
            entities.forEach((propEntity) => {
                if (propEntity.id) {
                    self.selectedEntities.push(propEntity.id);
                }
            });
        },

        runQuery: flow(function* (): any {
            const { searchService, messageService, historyService } =
                getEnv<Env>(self);

            const root = getRoot(self) as Instance<typeof RootStore>;

            if (!self.isSearching && self.query) {
                self.isSearching = true;
                const entities = yield searchService.findEntities(
                    self.query,
                    messageService!,
                );
                self.entities.replace(entities);
                self.calculateTypeCounts();
                self.selectedType = self.getMajorityType();
                self.selectedEntities.replace([]);
                self.isSearching = false;
                yield historyService.create(
                    {
                        id: '0',
                        query: self.query,
                        datetime: '',
                        results: self.entities,
                    },
                    messageService!,
                );

                const history = yield historyService.getAllAsOptions(
                    messageService!,
                );
                root.historySearchStore.setHistory(history);
            }
            self.selectedEntities.clear();
        }),
    }));