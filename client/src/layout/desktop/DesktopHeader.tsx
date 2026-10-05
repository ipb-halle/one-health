import { observer } from "mobx-react-lite";
import { Menubar } from "primereact/menubar";
import DesktopStartComponent from "./DesktopStartComponent";
import { MenuItem } from "primereact/menuitem";
import { RootStoreContext } from "@/app/providers/store-provider";
import { useContext } from "react";
import { logout } from '@/generated/auth/auth/auth';
import { useNavigate } from "react-router-dom";

const DesktopHeader: React.FC = () => {
    const rootStore = useContext(RootStoreContext);
    const authStore = rootStore.authStore;
    const navigate = useNavigate();

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
    return <div className="fluid fixed-top">
        <Menubar
            model={[legalItems, visItems, authItems]}
            start={<DesktopStartComponent />}
            pt={{ start: { style: { marginRight: 'auto' } } }}
        />
    </div>

}


export default observer(DesktopHeader);