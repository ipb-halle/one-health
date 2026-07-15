import React, { useContext } from 'react';
import './footer.component.scss';
import FooterIcons from './footerIcons';

import { observer } from 'mobx-react-lite';
import { RootStoreContext } from '@/app/providers/store-provider';


function Footer() {
   
    const screenDeviceStore = useContext(RootStoreContext).screenDeviceStore;

    const footerIconComponent = screenDeviceStore.isMobile ?
        <div className="ABCDE"><FooterIcons /><FooterIcons /></div> :
        <FooterIcons />

    return (
        <div className="app-footer">

            {footerIconComponent}

        </div>
    );
};

export default observer(Footer);
