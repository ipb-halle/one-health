import { createContext, ReactNode, useMemo } from 'react';
import { IRootStore, RootStore } from '../../store/root-store';
import { dependencyFactory } from '../di';
import { SERVICES } from '@/app/di/service-types';

export const RootStoreContext = createContext<IRootStore>(null as unknown as IRootStore);

export const StoreProvider = ({ children }: { children: ReactNode }) => {
    // useMemo stellt sicher, dass der Store nur einmal erstellt wird
    // und erst wenn die Komponente rendert (DI ist dann sicher fertig)
    const store = useMemo(() => RootStore.create({}, {
        searchService: dependencyFactory.get(SERVICES.IGeneralSearchService),
        historyService: dependencyFactory.get(SERVICES.IGeneralSearchHistoryService),
        messageService: dependencyFactory.get(SERVICES.MessageService),
    }), []);

    return (
        <RootStoreContext.Provider value={store}>
            {children}
        </RootStoreContext.Provider>
    );
};
