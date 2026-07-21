import  { useContext } from 'react';
import './footer.component.scss';

import { observer } from 'mobx-react-lite';
import { RootStoreContext } from '@/app/providers/store-provider';
import FooterIconsMobile from './FooterIconsMobile';
import FooterIconsDesktop from './FooterIconsDesktop';


function Footer() {

    const screenDeviceStore = useContext(RootStoreContext).screenDeviceStore;

    const footerIconComponent = screenDeviceStore.isMobile ?
       <FooterIconsMobile /> :
        <FooterIconsDesktop />

    return (
        <div className="app-footer">
            {footerIconComponent}
        </div>
    );
};

export default observer(Footer);
