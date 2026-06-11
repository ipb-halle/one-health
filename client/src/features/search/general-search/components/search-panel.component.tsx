import { useContext, useRef, useState } from 'react';
import React from 'react';
import { LoadingPlaceholderComponent } from '../../../../shared/components';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { dependencyFactory } from '../../../../app/di';
import { ITutorialStore, STORES } from '../../../../store/inversify';
import { Link } from 'react-router-dom';
import GeneralSearchPageTourComponent from './general-search-tour.component';
import './general-search.component.scss';
import SearchResultsPanel from './search-results-table.component';
import { observer } from 'mobx-react-lite';
import { Toast } from 'primereact/toast';
import HistoryModal from '../../search-history/components/general-search-history-modal.component';
import { RootStoreContext } from '../../../../app/providers/store-provider';

const SearchPanel: React.FC = () => {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;
    const historySearchStore = useContext(RootStoreContext).historySearchStore;

    const tutorialStore = dependencyFactory.get<ITutorialStore>(
        STORES.ITutorialStore,
    );

    const [historyVisible, setHistoryVisible] = useState<boolean>(false);

    const [runTutorial, setRunTutorial] = useState<boolean>(
        tutorialStore.getShowGeneralSearchTutorial(),
    );

    const helpClickedHandler = () => {
        setRunTutorial(true);
    };

    const helpTourCallback = () => {
        setRunTutorial(false);
        historySearchStore.initHistory();
        tutorialStore.setShowGeneralSearchTutorial(false);
        // tutorialStore.setShowCoOccurrencesSummaryTutorial(false);
    };

    /* ### UNDER CONSTRUCTION ### 
   ### integration of component-search-modal in search panel ### 
    const [savedMolFile, setSavedMolFile] = useState<string | null>(null);
    const [editor, setEditor] = useState<any>(null);


    const handleHide = () => {
        if (editor) {
            const mol = editor.getMolFile();
            setSavedMolFile(mol);
        }
        generalSearchStore.setIsModalOpen(false);
    };

    const initEditor = () => {
        const newEditor = OpenChemLib.StructureEditor.createSVGEditor(
            'structureSearchEditor',
            1
        );

        if (savedMolFile) {
            newEditor.setMolFile(savedMolFile);
        }

        setEditor(newEditor);
    } */

    const toast = useRef(null);

    return (
        <div id="general-search-panel">
            <GeneralSearchPageTourComponent
                run={runTutorial}
                callback={helpTourCallback}></GeneralSearchPageTourComponent>

            <div className="general-search-header" id="general-search-header">
                <div
                    className="wrapper"
                    style={{
                        width: '80%',
                        justifyContent: 'center',
                        display: 'flex',
                        flexDirection: 'column',
                        marginTop: '20px',
                    }}>
                    <div style={{ display: 'flex' }}>
                        <div className="p-inputgroup general-search-header-input">

                            <InputText
                                style={{
                                    border: 'none',
                                    boxShadow: 'none',
                                }}
                                value={generalSearchStore.query}
                                onChange={(e) => {
                                    generalSearchStore.setQuery(e.target.value);
                                }}
                                onKeyDown={(e) => {
                                    if (e.key === 'Enter')
                                        generalSearchStore.runQuery();
                                }}
                                placeholder="Search in knowledge base (e.g. disease name, plant name, compound name, InChI key, ...)"
                            />
                            <Button
                                icon="pi pi-search"
                                className="p-button-rounded p-button-text"
                                onClick={(e) => generalSearchStore.runQuery()}
                                tooltip="Search in knowledge base"
                                tooltipOptions={{
                                    position: 'bottom',
                                    showDelay: 1000,
                                }}
                            />
                        </div>

                        <Button
                            id="page-title-help-button"
                            icon="pi pi-question-circle"
                            style={{ marginLeft: '5px' }}
                            onClick={helpClickedHandler}
                            tooltip={`Watch tutorial`}
                            tooltipOptions={{
                                position: 'bottom',
                                showDelay: 1000,
                            }}
                        />
                    </div>

                    {/* TODO: implement as Buttons?*/}
                    <div className="general-search-links">
                        <Toast ref={toast} />

                        <Link
                            to="/search/structure-search"
                            className="general-search-link">
                            <i
                                className="fa fa-atom"
                                style={{ fontSize: '1rem' }}></i>{' '}
                            Compound Search
                        </Link>
                        <Link
                            to="/"
                            className="general-search-link"
                            onClick={() => {
                                setHistoryVisible(true);
                            }}>
                            <i
                                className="pi pi-history"
                                style={{ fontSize: '1rem' }}></i>{' '}
                            Search History
                        </Link>
                        <HistoryModal
                            visible={historyVisible}
                            onHide={() => setHistoryVisible(false)}
                        />
                    </div>
                </div>
                <p style={{ marginTop: '20px' }}>
                    using an advanced data collection, exchange and analysis
                    platform, with focus on the flora and epidemiological needs
                    of Latin-America
                </p>
            </div>
            <div id="search-table">
                {generalSearchStore.isSearching && (
                    <LoadingPlaceholderComponent></LoadingPlaceholderComponent>
                )}
                {generalSearchStore.isSearching === false && (
                    <div className="general-search-table">
                        <SearchResultsPanel />
                    </div>
                )}{' '}
            </div>
        </div>
    );
};

export default observer(SearchPanel);
