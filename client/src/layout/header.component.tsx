import React, { useContext } from 'react';
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate, useLocation } from 'react-router-dom';

import oneHealthLogo from '../assets/logo-n1h.png';

import './header.component.scss';
import { RootStoreContext } from '@/app/providers/store-provider';
import { observer } from 'mobx-react-lite';
import { logout } from '@/generated/auth/auth/auth';

import MobileHeader from './MobileHeader';


const Header: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();


    const rootStore = useContext(RootStoreContext);
    const screenDeviceStore = rootStore.screenDeviceStore;
    const authStore = rootStore.authStore;

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
        label: authStore.label,
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
        return <MobileHeader />
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
