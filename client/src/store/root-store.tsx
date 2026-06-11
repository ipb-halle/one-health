import { Instance, types } from 'mobx-state-tree';
import { GeneralSearchStore } from '../features/search/general-search/general-search-store';
import { HistorySearchStore } from '../features/search/search-history/history-search-store';

export const RootStore = types
    .model('RootStore', {
        generalSearchStore: types.optional(GeneralSearchStore, {}),
        historySearchStore: types.optional(HistorySearchStore, {}),
    });

export interface IRootStore extends Instance<typeof RootStore> {}