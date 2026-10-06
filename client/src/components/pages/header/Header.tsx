import React, { useContext } from 'react';
import './Header.scss';
import { RootStoreContext } from '@/app/providers/store-provider';
import { observer } from 'mobx-react-lite';
import MobileHeader from './MobileHeader';
import DesktopHeader from './DesktopHeader';


const Header: React.FC = () => {
    const rootStore = useContext(RootStoreContext);
    const screenDeviceStore = rootStore.screenDeviceStore;

    return screenDeviceStore.isMobile ? <MobileHeader /> : <DesktopHeader />
};

export default observer(Header);
