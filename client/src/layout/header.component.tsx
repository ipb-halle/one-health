<<<<<<< HEAD
import React, { useContext } from 'react';
=======
import React, { useState } from 'react';
>>>>>>> origin/mobile-version
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate, useLocation } from 'react-router-dom';

import oneHealthLogo from '../assets/logo-n1h.png';

import './header.component.scss';
import { RootStoreContext } from '@/app/providers/store-provider';
import { observer } from 'mobx-react-lite';
import { PanelMenu } from 'primereact/panelmenu';
import { Sidebar } from 'primereact/sidebar';
import { Button } from 'primereact/button';

//import LoginDialog from '../app/components/auth/LoginDialog';
//import { authService } from '../app/services/auth.service';


import LoginDialog from '../app/components/auth/LoginDialog';
import RegisterDialog from '../app/components/auth/RegisterDialog';

import { authService } from '../app/services/auth.service';

const Header: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();

<<<<<<< HEAD

    const legalItems: MenuItem =
    {
        label: 'Legal',
        icon: 'pi pi-file',
        items: [
            {
                //    icon: 'pi pi-compass',
                label: 'Documentation',
                command: () => {
                    navigate('/documentation');
                },
            },
            {
                //     icon: 'pi pi-exclamation-triangle',
                label: 'Legal Information',
                command: () => {
                    navigate('/legal');
                },
            },
        ]
    };
=======
    const [loginVisible, setLoginVisible] = useState(false);
    const [registerVisible, setRegisterVisible] = useState(false);

    const [isLoggedIn, setIsLoggedIn] = useState(
        authService.isAuthenticated(),
    );

    const refreshAuth = () => {
        setIsLoggedIn(authService.isAuthenticated());
    };

    const handleLogout = () => {
        authService.logout();
        refreshAuth();
        navigate('/');
    };

    const baseItems: MenuItem[] = [
        {
            label: 'Visualization',
            icon: 'pi pi-chart-bar',
            items: [
                {
                    // icon: 'pi pi-compass',
                    label: 'Neighborhood Explorer',
                    command: () => {
                        navigate('/neighborhood-explorer');
                    },
                },
                {
                    // icon: 'fa fa-circle-nodes',
                    label: 'Co-occurrences Search',
                    command: () => {
                        navigate('/visualization/co-occurrence-search/');
                    },
                },
            ],
        },
        {
            label: 'Contribute',
            icon: 'pi pi-sitemap',
            items: [
                // {
                //     label: 'Overview',
                //     command: () => {
                //         navigate('/ontology/overview/');
                //     }
                //     // icon: 'pi pi-chart-bar'
                // },
                {
                    label: 'New Entity Type',
                    command: () => {
                        navigate('/entity-type-form');
                    },
                    // icon: 'pi pi-box'
                },
                {
                    label: 'New Link Type',
                    command: () => {
                        navigate('/link-type-form');
                    },
                    // icon: 'pi pi-arrows-h'
                },
                {
                    label: 'Data Load',
                    command: () => {
                        navigate('/ontology/data-load/0');
                    },
                },
            ],
        },
        // {
        //     label: 'Documentation',
        //     icon: 'pi pi-book',
        //     command: () => {
        //         navigate('/documentation');
        //     },
        // },
        // {
        //     label: 'Legal Information',
        //     icon: 'pi pi-exclamation-triangle',
        //     command: () => {
        //         navigate('/legal');
        //     },
        // },

        // {
        //     label: 'Contact',
        //     icon: 'pi pi-envelope',
        //     command: () => {
        //         navigate('/test');
        //     }
        // }

        !isLoggedIn
            ?
            {
                label: 'Login',
                icon: 'pi pi-sign-in',
                command: () => setLoginVisible(true),
            }
            :
            {
                label: 'Logout',
                icon: 'pi pi-sign-out',
                command: handleLogout,
            },
    ];
>>>>>>> origin/mobile-version


    const visItems: MenuItem = {
        label: 'Visualization',
        icon: 'pi pi-chart-line',
        items: [
            {
                // icon: 'pi pi-compass',
                label: 'Neighborhood Explorer',
                command: () => {
                    navigate('/neighborhood-explorer');
                },
            },
            {
                // icon: 'fa fa-circle-nodes',
                label: 'Co-occurrences Search',
                command: () => {
                    navigate('/visualization/co-occurrence-search/');
                },
            },
        ],
    };

    const items: MenuItem[] = [legalItems];
    const screenDeviceStore = useContext(RootStoreContext).screenDeviceStore;

    if (!screenDeviceStore.isMobile) {
        items.push(visItems);
    }

    if (location.pathname !== '/') {
        items.unshift({
            label: 'General Search',
            icon: 'pi pi-search',
            command: () => { screenDeviceStore.setMenuVisibility(false); navigate('/') },
        });
    }

    const start = (
        <div
            className="col"
            style={{
                display: 'flex',
                alignItems: 'center',
<<<<<<< HEAD
                gap: '40px',
                justifyContent: 'space-between',
=======
                gap: '40px'
>>>>>>> origin/mobile-version
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
            <Button icon="pi pi-bars" text label="Menu"
                visible={screenDeviceStore.isMobile}
                onClick={() => { screenDeviceStore.setMenuVisibility(true) }} />
        </div>
    );


    const menuBar = screenDeviceStore.isMobile ? <div className="p-menubar">
        {start}
        <Sidebar visible={screenDeviceStore.mobileMenuVisible}
            onHide={() => { screenDeviceStore.setMenuVisibility(false) }} >
            <PanelMenu model={items} onClick={() => { screenDeviceStore.setMenuVisibility(false) }} />
        </Sidebar>
    </div>
        : <Menubar
            model={items}
            start={start}
            pt={{ start: { style: { marginRight: 'auto' } } }}
        />

    return (
<<<<<<< HEAD
        <div className="fluid fixed-top">
            {/* <Menubar
=======
        <>
            <div className="fluid fixed-top">
                <Menubar
>>>>>>> origin/mobile-version
                    model={items}
                    start={start}
                    pt={{
                        start: {
                            style: {
                                marginRight: 'auto',
                            },
                        },
                    }}
<<<<<<< HEAD
                />*/}
            {menuBar}
        </div>
=======
                />
            </div>

            {/* LOGIN DIALOG */}
            <LoginDialog
                visible={loginVisible}
                onHide={() => setLoginVisible(false)}
                onSuccess={() => {
                    refreshAuth();
                    setLoginVisible(false);
                }}
                onRegisterClick={() => {
                    setLoginVisible(false);
                    setRegisterVisible(true);
                }}
            />

            {/* REGISTER DIALOG */}
            <RegisterDialog
                visible={registerVisible}
                onHide={() => setRegisterVisible(false)}
                onSuccess={() => {
                    setRegisterVisible(false);
                    setLoginVisible(true);
                }}
                onLoginClick={() => {
                    setLoginVisible(true);
                    setRegisterVisible(false);
                }}
            />
        </>

>>>>>>> origin/mobile-version
    );
};

export default observer(Header);
