import '../assets/styles/Style.scss';
import './app.component.scss';
import { RootStoreContext } from './providers/store-provider';
import { useContext, useEffect } from 'react';
import MessageServiceContext from './providers/messages/message-service.context';
import { DesktopApp } from './DesktopApp';
import { observer } from 'mobx-react-lite';
import MobileApp from './MobileApp';

function App() {

    const rootStore = useContext(RootStoreContext);
    const screenDeviceStore = rootStore.screenDeviceStore;
    const authStore = rootStore.authStore;
    const { messageService } = useContext(MessageServiceContext);

    useEffect(
        () => {
            screenDeviceStore.start();
            return () => screenDeviceStore.stop();
        },
        [screenDeviceStore]);

    useEffect(() => {
        void authStore.loadCurrentUser();
    }, [authStore]);

    // Check for authentication failure on initial render
    useEffect(() => {
        if (screenDeviceStore.isMobile) return; // Skip authentication error check on mobile devices

        const params = new URLSearchParams(window.location.search);
        const errorCode = params.get('auth_error');
        const errorDescription = params.get('auth_message');

        if (errorCode) {
            // Clean URL immediately so error doesn't re-trigger on refresh
            window.history.replaceState({}, "", window.location.pathname);

            if (messageService) {
                messageService.show({
                    severity: 'error',
                    summary: errorCode.replace(/_/g, ' ').toUpperCase(), // e.g., "ACCESS_DENIED" -> "ACCESS DENIED"
                    detail: errorDescription || 'An unexpected authentication error occurred.',
                });
            }
        }
    }, [messageService, screenDeviceStore.isMobile]);


    const appComponent = screenDeviceStore.isMobile ?
        <MobileApp /> :
        <DesktopApp />

    return appComponent;
}

export default observer(App);
