import React, { useContext } from 'react';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate, useLocation } from 'react-router-dom';
import './header.component.scss';
import { RootStoreContext } from '@/app/providers/store-provider';
import { observer } from 'mobx-react-lite';
import MobileHeader from './mobile/MobileHeader';
import DesktopHeader from './desktop/DesktopHeader';


const Header: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const rootStore = useContext(RootStoreContext);
    const screenDeviceStore = rootStore.screenDeviceStore;

    const items: MenuItem[] = [];


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

    return screenDeviceStore.isMobile ? <MobileHeader /> : <DesktopHeader />
};

export default observer(Header);
