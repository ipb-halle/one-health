import { useContext } from 'react';
import { RootStoreContext } from '../../../../app/providers/store-provider';
import { Button } from 'primereact/button';

import { observer } from 'mobx-react-lite';

// we decided not to use this component and use only the modal because its more intuitive

const HistoryTokenList = () => {
    const historySearchStore = useContext(RootStoreContext).historySearchStore;
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;

    return (
        <div className="token-list">
            {historySearchStore
                .getHistoryAsJSON()
                .slice(0, 3)
                .map((item) => (
                    <div
                        key={item.id}
                        className="token"
                        onClick={(e) => {
                            if (item.query)
                                generalSearchStore.setQuery(item.query);
                        }}>
                        <span className="mb-0">{item.query}</span>
                        <Button
                            className="token-button"
                            text
                            rounded
                            size="small"
                            icon="pi pi-times"
                            onClick={async (e) => {
                                e.stopPropagation();
                                historySearchStore.deleteHistoryItem(item);
                            }}
                            pt={{ icon: { style: { color: 'black' } } }}
                            tooltip="Remove from history"
                            tooltipOptions={{
                                position: 'bottom',
                                showDelay: 1000,
                            }}
                        />
                    </div>
                ))}
        </div>
    );
};

export default observer(HistoryTokenList);
