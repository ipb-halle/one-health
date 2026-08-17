import { RootStoreContext } from "@/app/providers/store-provider";
import CompactResultDisplay from "@/features/search/general-search/components/compact-result-display";
import GeneralSearchPageTourComponent from "@/features/search/general-search/components/general-search-tour.component";
import { dependencyFactory } from "@/app/di";
import { ITutorialStore, STORES } from "@/store/inversify";
import { observer } from "mobx-react-lite";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import earthImage from '../../assets/img/earth_image.png';

import './mobile-home-page.component.scss';

function MobileHomePageComponent() {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;
    const historySearchStore = useContext(RootStoreContext).historySearchStore;
    const navigate = useNavigate();

    const tutorialStore = dependencyFactory.get<ITutorialStore>(
        STORES.ITutorialStore,
    );

    const [runTutorial, setRunTutorial] = useState<boolean>(false);

    const helpClickedHandler = () => {
        setRunTutorial(true);
    };

    const helpTourCallback = () => {
        setRunTutorial(false);
        historySearchStore.initHistory();
        tutorialStore.setShowGeneralSearchTutorial(false);
    };

    const handleClearSearch = () => {
        generalSearchStore.setQuery('');
        generalSearchStore.setIsSearching(null as any);
    };

    const isSearchingActive = generalSearchStore.isSearching !== null;

    return (
        <div className="mobile-landing-container">
            <GeneralSearchPageTourComponent
                run={runTutorial}
                callback={helpTourCallback}
            />

            {isSearchingActive ? (
                <div className="mobile-results-wrapper">
                    <div className="mobile-search-bar-section">
                        <div className="mobile-search-input-wrapper">
                            <InputText
                                className="mobile-search-input"
                                value={generalSearchStore.query}
                                onChange={(e) => generalSearchStore.setQuery(e.target.value)}
                                onKeyDown={(e) => {
                                    if (e.key === 'Enter') generalSearchStore.runQuery();
                                }}
                                placeholder="Search in knowledge base (e.g. disease name, ...)"
                            />
                            <button
                                className="mobile-search-btn"
                                onClick={() => generalSearchStore.runQuery()}
                                aria-label="Search"
                                title="Search"
                            >
                                <i className="pi pi-search" />
                            </button>
                            <button
                                className="mobile-search-info-btn"
                                onClick={helpClickedHandler}
                                aria-label="Tutorial and Info"
                                title="Watch tutorial"
                            >
                                <i className="pi pi-info-circle" />
                            </button>
                        </div>
                    </div>
                    <div className="mobile-results-header">
                        <Button
                            label="Back to overview"
                            icon="pi pi-arrow-left"
                            className="p-button-text p-button-sm mobile-back-btn"
                            onClick={handleClearSearch}
                        />
                    </div>
                    <CompactResultDisplay />
                </div>
            ) : (
                <div className="mobile-main-content">
                    {/* KNOWLEDGE-BASE SEARCH */}
                    <div className="mobile-search-bar-section">
                        <div className="mobile-search-input-wrapper">
                            <InputText
                                className="mobile-search-input"
                                value={generalSearchStore.query}
                                onChange={(e) => generalSearchStore.setQuery(e.target.value)}
                                //onKeyDown={(e) => {
                                //  if (e.key === 'Enter') generalSearchStore.runQuery();
                                // }}

                                placeholder="Search in knowledge base (e.g. disease name, ...)"
                            />
                            <button
                                className="mobile-search-btn"
                                //onClick={() => generalSearchStore.runQuery()}
                                aria-label="Search"
                                title="Search"
                            >
                                <i className="pi pi-search" />
                            </button>
                            <button
                                className="mobile-search-info-btn"
                                // onClick={helpClickedHandler}
                                aria-label="Tutorial and Info"
                                title="Watch tutorial"
                            >
                                <i className="pi pi-info-circle" />
                            </button>
                        </div>
                    </div>

                    {/* HERO / INTRODUCTION */}
                    <div className="mobile-hero-card">
                        <div className="hero-left-col">
                            <img
                                src={earthImage}
                                alt="One Health Earth Illustration"
                                className="hero-earth-img"
                            />
                        </div>
                    </div>

                    <div className="mobile-hero-card">
                        <div className="hero-right-col">
                            <h2 className="hero-heading">
                                Empowering the research on plant-derived natural products for the treatment of diseases
                            </h2>
                            {/*<p className="hero-subtext">
                                Search connections across plants, compounds and diseases in integrated datasets.
                            </p>*/}
                        </div>
                    </div>



                    {/* PLATFORM STATISTICS */}
                    <div className="mobile-stats-grid">
                        <div className="stat-card">
                            <div className="stat-icon-box">
                                <i className="fa-regular fa-file-lines stat-icon" />
                            </div>
                            <div className="stat-text-box">
                                <span className="stat-value">25,000+</span>
                                <span className="stat-label">Publications</span>
                            </div>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon-box">
                                <i className="fa-solid fa-seedling stat-icon" />
                            </div>
                            <div className="stat-text-box">
                                <span className="stat-value">3,400+</span>
                                <span className="stat-label">Plant Species</span>
                            </div>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon-box">
                                <i className="fa-solid fa-atom stat-icon" />
                            </div>
                            <div className="stat-text-box">
                                <span className="stat-value">8,200+</span>
                                <span className="stat-label">Natural Compounds</span>
                            </div>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon-box">
                                <i className="fa-solid fa-virus stat-icon" />
                            </div>
                            <div className="stat-text-box">
                                <span className="stat-value">1,500+</span>
                                <span className="stat-label">Diseases</span>
                            </div>
                        </div>
                    </div>

                </div>
            )}
        </div>
    );
}

export default observer(MobileHomePageComponent);
