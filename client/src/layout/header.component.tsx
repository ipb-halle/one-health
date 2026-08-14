import React, { useContext, useState } from 'react';
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate, useLocation } from 'react-router-dom';

import oneHealthLogo from '../assets/logo-n1h.png';

import './header.component.scss';
import { RootStoreContext } from '@/app/providers/store-provider';
import { observer } from 'mobx-react-lite';
import { PanelMenu } from 'primereact/panelmenu';
import { Sidebar } from 'primereact/sidebar';

import LoginDialog from '../app/components/auth/LoginDialog';
import RegisterDialog from '../app/components/auth/RegisterDialog';
import HistoryModal from '@/features/search/search-history/components/general-search-history-modal.component';

const Header: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const screenDeviceStore = useContext(RootStoreContext).screenDeviceStore;

    const [loginVisible, setLoginVisible] = useState<boolean>(false);
    const [registerVisible, setRegisterVisible] = useState<boolean>(false);
    const [historyVisible, setHistoryVisible] = useState<boolean>(false);

    const legalItems: MenuItem = {
        label: 'Legal',
        icon: 'pi pi-file',
        items: [
            {
                label: 'Documentation',
                command: () => {
                    navigate('/documentation');
                },
            },
            {
                label: 'Legal Information',
                command: () => {
                    navigate('/legal');
                },
            },
        ],
    };

    const visItems: MenuItem = {
        label: 'Visualization',
        icon: 'pi pi-chart-line',
        items: [
            {
                label: 'Neighborhood Explorer',
                command: () => {
                    navigate('/neighborhood-explorer');
                },
            },
            {
                label: 'Co-occurrences Search',
                command: () => {
                    navigate('/visualization/co-occurrence-search/');
                },
            },
        ],
    };

    const items: MenuItem[] = [legalItems];

    if (!screenDeviceStore.isMobile) {
        items.push(visItems);
    }

    if (location.pathname !== '/') {
        items.unshift({
            label: 'General Search',
            icon: 'pi pi-search',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/');
            },
        });
    }

    const mobileMenuItems: MenuItem[] = [
        {
            label: 'Home',
            icon: 'pi pi-home',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/');
            },
        },
        {
            label: 'Compound Search',
            icon: 'fa fa-atom',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/search/structure-search');
            },
        },
        {
            label: 'Neighborhood Explorer',
            icon: 'pi pi-compass',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/neighborhood-explorer');
            },
        },
        {
            label: 'Co-occurrences Search',
            icon: 'pi pi-chart-line',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/visualization/co-occurrence-search/');
            },
        },
        {
            label: 'Search History',
            icon: 'pi pi-history',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                setHistoryVisible(true);
            },
        },
        {
            label: 'Documentation',
            icon: 'pi pi-book',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/documentation');
            },
        },
        {
            label: 'Legal Information',
            icon: 'pi pi-file',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/legal');
            },
        },
    ];

    const desktopStart = (
        <div
            className="col"
            style={{
                display: 'flex',
                alignItems: 'center',
                gap: '40px',
                justifyContent: 'space-between',
            }}
        >
            <a href="/">
                <img
                    alt="logo"
                    src={oneHealthLogo}
                    height="35"
                    style={{ marginLeft: 20 }}
                    className="mr-2"
                />
            </a>
        </div>
    );

    if (screenDeviceStore.isMobile) {
        return (
            <div className="fluid fixed-top">
                <header className="mobile-header">
                    <div className="mobile-header-left">
                        <button
                            className="mobile-menu-btn"
                            onClick={() => screenDeviceStore.setMenuVisibility(true)}
                            aria-label="Toggle menu"
                        >
                            <i className="pi pi-bars" />
                        </button>
                        <a href="/" className="mobile-brand">
                            <img alt="One Health logo" src={oneHealthLogo} className="mobile-logo-img" />
                        </a>
                    </div>

                    <div className="mobile-header-right">
                        <button
                            className="mobile-shortcut-btn"
                            onClick={() => navigate('/search/structure-search')}
                            title="Compound Search"
                        >
                            <i className="fa-solid fa-atom shortcut-icon" />
                            <span className="shortcut-label">Compound Search</span>
                        </button>

                        <button
                            className="mobile-shortcut-btn"
                            onClick={() => setHistoryVisible(true)}
                            title="Search History"
                        >
                            <i className="pi pi-history shortcut-icon" />
                            <span className="shortcut-label">Search History</span>
                        </button>

                        <button
                            className="mobile-shortcut-btn mobile-user-btn"
                            onClick={() => setLoginVisible(true)}
                            title="User Profile / Login"
                        >
                            <i className="pi pi-user shortcut-icon" />
                        </button>
                    </div>
                </header>

                <Sidebar
                    visible={screenDeviceStore.mobileMenuVisible}
                    onHide={() => screenDeviceStore.setMenuVisibility(false)}
                >
                    <div className="mobile-sidebar-header" style={{ padding: '10px 0 15px 0', borderBottom: '1px solid #e2e8f0', marginBottom: '15px', display: 'flex', alignItems: 'center', gap: '10px' }}>
                        <img alt="logo" src={oneHealthLogo} height="30" />
                        <span style={{ fontWeight: 'bold', fontSize: '1.1rem', color: '#166534' }}>ONE HEALTH</span>
                    </div>
                    <PanelMenu model={mobileMenuItems} onClick={() => screenDeviceStore.setMenuVisibility(false)} />
                </Sidebar>

                <HistoryModal
                    visible={historyVisible}
                    onHide={() => setHistoryVisible(false)}
                />
                <LoginDialog
                    visible={loginVisible}
                    onHide={() => setLoginVisible(false)}
                    onSuccess={() => setLoginVisible(false)}
                    onRegisterClick={() => {
                        setLoginVisible(false);
                        setRegisterVisible(true);
                    }}
                />
                <RegisterDialog
                    visible={registerVisible}
                    onHide={() => setRegisterVisible(false)}
                    onSuccess={() => setRegisterVisible(false)}
                    onLoginClick={() => {
                        setRegisterVisible(false);
                        setLoginVisible(true);
                    }}
                />
            </div>
        );
    }

    return (
        <div className="fluid fixed-top">
            <Menubar
                model={items}
                start={desktopStart}
                pt={{ start: { style: { marginRight: 'auto' } } }}
            />
        </div>
    );
};

export default observer(Header);
