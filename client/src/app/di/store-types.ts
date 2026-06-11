// This file contains the store symbols used for dependency injection

const STORES = {
    ITutorialStore: Symbol.for('ITutorialStore'),
    INeighborhoodExplorerStore: Symbol.for('INeighborhoodExplorerStore'),
    ILocalStorageStore: Symbol.for('ILocalStorageStore'),
};

export { STORES };
