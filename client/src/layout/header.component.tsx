import React, { useContext } from 'react';
import './header.component.scss';
import { RootStoreContext } from '@/app/providers/store-provider';
import { observer } from 'mobx-react-lite';
import MobileHeader from './mobile/MobileHeader';
import DesktopHeader from './desktop/DesktopHeader';


const Header: React.FC = () => {
    const rootStore = useContext(RootStoreContext);
    const screenDeviceStore = rootStore.screenDeviceStore;

    return screenDeviceStore.isMobile ? <MobileHeader /> : <DesktopHeader />
};

export default observer(Header);
