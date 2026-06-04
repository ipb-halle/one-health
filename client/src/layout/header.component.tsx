import React, { useState } from 'react';
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate, useLocation } from 'react-router-dom';

import oneHealthLogo from '../assets/logo-n1h.png';

import './header.component.scss';


import LoginDialog from '../app/components/auth/LoginDialog';
import RegisterDialog from '../app/components/auth/RegisterDialog';

import { authService } from '../app/services/auth.service';

const Header: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();

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

    const items: MenuItem[] = [...baseItems];

    if (location.pathname !== '/') {
        items.unshift({
            label: 'General Search',
            icon: 'pi pi-search',
            command: () => navigate('/'),
        });
    }

    const start = (
        <div
            className="col"
            style={{
                display: 'flex',
                alignItems: 'center',
                gap: '40px'
            }}
        >
            <a href="/">
                <img
                    alt="logo"
                    src={oneHealthLogo}
                    height="40"
                    style={{ marginLeft: 20 }}
                    className="mr-2"
                />
            </a>
            <p style={{ fontSize: '16px', color: '#a40', margin: 0 }}>
                This service is <strong>work in progress</strong>, layout and
                function are subject to change.
            </p>
        </div>
    );

    return (
        <>
            <div className="fluid fixed-top">
                <Menubar
                    model={items}
                    start={start}
                    pt={{
                        start: {
                            style: {
                                marginRight: 'auto',
                            },
                        },
                    }}
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

    );
};

export default Header;
