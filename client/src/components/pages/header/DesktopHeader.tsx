import { observer } from "mobx-react-lite";
import { Menubar } from "primereact/menubar";
import { MenuItem } from "primereact/menuitem";
import { RootStoreContext } from "@/app/providers/store-provider";
import { useContext, useEffect } from "react";
import { logout } from '@/generated/auth/auth/auth';
import { useNavigate } from "react-router-dom";
import DesktopStartComponent from "./DesktopStartComponent";
import MessageServiceContext from "@/app/providers/messages/message-service.context";

const DesktopHeader: React.FC = () => {
    const rootStore = useContext(RootStoreContext);
    const authStore = rootStore.authStore;
    const navigate = useNavigate();
    const screenDeviceStore = rootStore.screenDeviceStore;
    const { messageService } = useContext(MessageServiceContext);

    // Check for authentication failure on initial render
    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const errorCode = params.get('auth_error');
        const errorDescription = params.get('auth_message');

        if (errorCode) {
            // Clean URL immediately so error doesn't re-trigger on refresh
            window.history.replaceState({}, document.title, window.location.pathname);

            if (messageService) {
                messageService.show({
                    severity: 'error',
                    summary: errorCode.replace(/_/g, ' ').toUpperCase(), // e.g., "ACCESS_DENIED" -> "ACCESS DENIED"
                    detail: errorDescription || 'An unexpected authentication error occurred.',
                });
            }
        }
    }, [messageService]);

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
    const items: MenuItem[] = [legalItems, visItems, authItems];
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

    return <div className="fluid fixed-top">
        <Menubar
            model={items}
            start={<DesktopStartComponent />}
            pt={{ start: { style: { marginRight: 'auto' } } }}
        />
    </div>

}


export default observer(DesktopHeader);