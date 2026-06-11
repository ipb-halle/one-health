import {
    types,
    flow,
    Instance,
    getEnv,
    getSnapshot,
    SnapshotOut,
} from 'mobx-state-tree';
import React from 'react';
import { IGeneralSearchHistoryService } from '../../../features/search/search-history/services/general-search-history-service';
import { IGeneralSearchService } from '../../../features/search/general-search/services/general-search-service';
import type { MessageService } from '../../../core/api/messages/interfaces/message-service';
import { SavedTextSearch } from '../../../features/search/search-history/models/SavedTextSearch';

type Env = {
    historyService: IGeneralSearchHistoryService;
    messageService: MessageService;
    searchService: IGeneralSearchService;
};

export const HistorySearchStore = types
    .model({
        history: types.array(SavedTextSearch),
    })
    .views((self) => ({
        getHistorySize() {
            return self.history.length;
        },
        getHistoryAsJSON(): SnapshotOut<typeof SavedTextSearch>[] {
            return self.history.map((item) => getSnapshot(item));
        },
    }))
    .actions((self) => ({
        initHistory: flow(function* () {
            const { historyService, messageService } = getEnv<Env>(self);
            const data = yield historyService.getAllAsOptions(messageService);
            self.history = data;
        }),
        setHistory(history: Instance<typeof SavedTextSearch>[]): void {
            self.history.replace(history);
        },

        deleteHistoryItem: flow(function* (
            item: Instance<typeof SavedTextSearch>,
        ): any {
            const { messageService, historyService } = getEnv<Env>(self);

            if (item.id != undefined) yield historyService.delete(item.id);
            self.history = yield historyService.getAllAsOptions(
                messageService!,
            );
        }),
        clearHistory: flow(function* (): any {
            const { messageService, historyService } = getEnv<Env>(self);

            for (const item of self.history) {
                if (item.id != undefined) {
                    yield historyService.delete(item.id);
                }
            }

            self.history = yield historyService.getAllAsOptions(
                messageService!,
            );
        }),
    }));