import { injectable } from 'inversify';

export const LOCAL_STORAGE_KEYS = {
    // tutorial keys
    showNeighborhoodExplorerTutorial:
        'one-health-show-neighborhood-explorer-tutorial',
    showCoOccurrencesSummaryTutorial:
        'one-health-show-cooccurrences-summary-tutorial',
    showCompoundSearchTutorial: 'one-health-show-compound-search-tutorial',
    showGeneralSearchTutotrial: 'one-health-show-general-search-tutorial',

    // warning messages
    showNeighborhoodExplorerWarning:
        'one-health-show-neighborhood-explorer-warning',
    showCoOccurrencesSummaryWarning:
        'one-health-show-cooccurrences-summary-warning',
};

@injectable()
export class ILocalStorageStore {
    setBooleanKeyValue(key: string, value: boolean): void {
        throw Error();
    }
    getBooleanKeyValue(key: string): boolean {
        throw Error();
    }
}

@injectable()
export class LocalStorageStore extends ILocalStorageStore {
    constructor() {
        super();
    }

    setBooleanKeyValue(key: string, value: boolean): void {
        localStorage.setItem(key, value.valueOf().toString());
    }

    getBooleanKeyValue(key: string): boolean {
        const result = localStorage.getItem(key);
        if (result == null) {
            this.setBooleanKeyValue(key, true);
            return true;
        } else {
            return result === 'true';
        }
    }
}
