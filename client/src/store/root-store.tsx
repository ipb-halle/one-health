import { Instance, types } from 'mobx-state-tree';
import { GeneralSearchStore } from '../features/search/general-search/general-search-store';
import { HistorySearchStore } from '../features/search/search-history/history-search-store';
import { ScreenDeviceStore } from './ScreenDeviceStore';
import { EntityDetailStore } from './EntityDetailStore';
import { TutorialStore } from './TutorialStore';

export const RootStore = types
    .model('RootStore', {
        generalSearchStore: types.optional(GeneralSearchStore, {}),
        historySearchStore: types.optional(HistorySearchStore, {}),
        screenDeviceStore: types.optional(ScreenDeviceStore, {}),
        entityDetailStore: types.optional(EntityDetailStore, {}),
        tutorialStore: types.optional(TutorialStore, {}),
    });

export interface IRootStore extends Instance<typeof RootStore> {}