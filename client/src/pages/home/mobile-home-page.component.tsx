import { RootStoreContext } from "@/app/providers/store-provider";
import CompactResultDisplay from "@/features/search/general-search/components/compact-result-display";
//import GeneralSearchPageTourComponent from "@/features/search/general-search/components/general-search-tour.component";
//import { dependencyFactory } from "@/app/di";
//import { ITutorialStore, STORES } from "@/store/inversify";
import { observer } from "mobx-react-lite";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { useContext, useState } from "react";
//import { useNavigate } from "react-router-dom";
import earthImage from '../../assets/img/earth_image.png';

import './mobile-home-page.component.scss';

const statistics = [{ value: "25,000+", label: "Publications", icon: "fa-regular fa-file-lines", }, { value: "3,400+", label: "Plant Species", icon: "fa-solid fa-seedling", }, { value: "8,200+", label: "Natural Compounds", icon: "fa-solid fa-atom", }, { value: "1,500+", label: "Diseases", icon: "fa-solid fa-virus", },];

function MobileHomePageComponent() {
    const generalSearchStore = useContext(RootStoreContext).generalSearchStore;
    //  const historySearchStore = useContext(RootStoreContext).historySearchStore;

    const isSearchingActive = generalSearchStore.isSearching !== null;

    const handleSearch = () => {
        generalSearchStore.runQuery();
    }

    const handleKeyDown =
        (e: React.KeyboardEvent<HTMLInputElement>) => {
            if (e.key === "Enter") {
                handleSearch();
            }
        };

    const handleClearSearch = () => {
        generalSearchStore.setQuery('');
        generalSearchStore.setIsSearching(null as any);
    };

    const searchBar = (
        <div className="mobile-search-bar-section">
            <div className="mobile-search-input-wrapper">
                <InputText
                    className="mobile-search-input"
                    value={generalSearchStore.query}
                    onChange={(e) => generalSearchStore.setQuery(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="Search in knowledge base (e.g. disease name, ...)"
                />
                <button
                    type="button"
                    className="mobile-search-btn"
                    onClick={handleSearch}
                    aria-label="Search"
                    title="Search"
                >
                    <i className="pi pi-search" />
                </button>
            </div>
        </div>
    );


    return (
        <div className="mobile-landing-container">

            {isSearchingActive ? (
                <div className="mobile-results-wrapper">
                    {searchBar}

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
                    {searchBar}

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
                                Empowering the research on plant-derived
                                natural products for the treatment of diseases
                            </h2>
                        </div>
                    </div>

                    {/* PLATFORM STATISTICS */}
                    <div className="mobile-stats-grid">
                        {statistics.map((stat) => (
                            <div
                                className="stat-card"
                                key={stat.label}
                            >

                                <div className="stat-icon-box">
                                    <i
                                        className={`${stat.icon} stat-icon`}
                                    />
                                </div>

                                <div className="stat-text-box">
                                    <span className="stat-value">
                                        {stat.value}
                                    </span>
                                    <span className="stat-label">
                                        {stat.label}
                                    </span>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
}

export default observer(MobileHomePageComponent);
