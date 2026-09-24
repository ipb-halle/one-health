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

import HistoryModal from '@/features/search/search-history/components/general-search-history-modal.component';
import { logout } from '@/generated/auth/auth/auth';


const Header: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();


    const rootStore = useContext(RootStoreContext);
    const screenDeviceStore = rootStore.screenDeviceStore;
    const authStore = rootStore.authStore;

    const [historyVisible, setHistoryVisible] = useState<boolean>(false);
    const user = authStore.user;

    const getInitials = (displayName?: string): string => {
        if (!displayName) {
            return 'U';
        }
        const parts = displayName.trim().split(/\s+/);
        if (parts.length === 1) {
            return parts[0].charAt(0).toUpperCase();
        }
        return (
            parts[0].charAt(0) +
            parts[parts.length - 1].charAt(0)
        ).toUpperCase();
    }
    const userInitials = getInitials(user?.displayName);

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

    const authItems: MenuItem = {
        label: authStore.isAuthenticated
            ? `${userInitials} ${user?.displayName ?? 'User'} . Log out`
            : 'Sign in with ORCID',
        icon: 'pi pi-user',
        command: async () => {
            if (authStore.isAuthenticated) {
                try {
                    await logout();
                    authStore.clearUser();
                    window.location.reload();
                } catch (error) {
                    console.error('Logout failed: ', error);
                }
            }
            else {
                window.location.href = '/api/oauth2/authorization/orcid';
            }
        },
    };

    const items: MenuItem[] = [legalItems];

    if (!screenDeviceStore.isMobile) {
        items.push(visItems);
        items.push(authItems);
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
                            className="mobile-shortcut-btn mobile-user-btn"
                            onClick={async () => {
                                if (authStore.isAuthenticated) {
                                    try {
                                        await logout();
                                        authStore.clearUser();
                                        window.location.reload();
                                    } catch (error) {
                                        console.error('Logout failed: ', error);
                                    }
                                }
                                else {
                                    window.location.href = '/api/oauth2/authorization/orcid';
                                }
                            }}
                            title={
                                authStore.isAuthenticated
                                    ? `Log out ${user?.displayName ?? ''}`
                                    : 'Sign in with ORCID'
                            }
                        >
                            {authStore.isAuthenticated ? (
                                <span className="shortcut-icon">{userInitials}</span>
                            ) : (
                                <i className="pi pi-user shortcut-icon" />
                            )}
                        </button>



                    </div>
                </header>

                <Sidebar
                    visible={screenDeviceStore.mobileMenuVisible}
                    onHide={() => screenDeviceStore.setMenuVisibility(false)}
                >
                    <div className="mobile-sidebar-header" style={{ padding: '10px 0 15px 0', borderBottom: '1px solid #e2e8f0', marginBottom: '15px', display: 'flex', alignItems: 'center', gap: '10px' }}>
                        <img alt="logo" src={oneHealthLogo} height="30" />
                    </div>
                    <PanelMenu model={mobileMenuItems} onClick={() => screenDeviceStore.setMenuVisibility(false)} />
                </Sidebar>

                <HistoryModal
                    visible={historyVisible}
                    onHide={() => setHistoryVisible(false)}
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
