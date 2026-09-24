import '../assets/styles/Style.scss';
import './app.component.scss';
import { RootStoreContext } from './providers/store-provider';
import { useContext, useEffect } from 'react';
import { DesktopApp } from './DesktopApp';
import { observer } from 'mobx-react-lite';
import MobileApp from './MobileApp';

function App() {

    const rootStore = useContext(RootStoreContext);
    const screenDeviceStore = rootStore.screenDeviceStore;
    const authStore = rootStore.authStore;

    useEffect(
        () => {
            screenDeviceStore.start();
            return () => screenDeviceStore.stop();
        },
        [screenDeviceStore]);

    useEffect(() => {
        void authStore.loadCurrentUser();
    }, [authStore]);


    const appComponent = screenDeviceStore.isMobile ?
        <MobileApp /> :
        <DesktopApp />

    return appComponent;
}

export default observer(App);
