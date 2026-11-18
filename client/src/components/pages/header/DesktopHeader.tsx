import { observer } from "mobx-react-lite";
import { Menubar } from "primereact/menubar";
import { Menu } from "primereact/menu";
import { Button } from "primereact/button";
import type { MenuItem } from "primereact/menuitem";
import { RootStoreContext } from "@/app/providers/store-provider";
import { useContext, useRef } from "react";
import { logout } from '@/generated/auth/auth/auth';
import { useLocation, useNavigate } from "react-router-dom";
import DesktopStartComponent from "./DesktopStartComponent";



const DesktopHeader: React.FC = () => {
    const rootStore = useContext(RootStoreContext);
    const authStore = rootStore.authStore;
    const navigate = useNavigate();
    const screenDeviceStore = rootStore.screenDeviceStore;
    const location = window.location;

    const userMenuRef = useRef<Menu>(null);

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

    const userMenuItems: MenuItem[] = [
        {
            label: 'My Profile',
            icon: 'pi pi-user',
            command: () => {
                navigate('/profile');
            },
        },
        {
            separator: true,
        },
        {
            label: 'Logout',
            icon: 'pi pi-sign-out',
            command: () => {
                void authStore.loginOrLogout(window);
            },
        },
    ];

    const items: MenuItem[] = [legalItems, visItems];

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

    if (!authStore.isAuthenticated) {
        items.push({
            label: authStore.label,
            icon: "pi pi-user",
            command: () => {
                void authStore.loginOrLogout(window);
            },
        });
    }

    return (
        <div className="fluid fixed-top">
            <Menubar
                model={items}
                start={<DesktopStartComponent />}
                end={
                    authStore.isAuthenticated ? (
                        <div className="desktop-user-menu">
                            <span>
                                Hi {authStore.user?.displayName}
                            </span>
                            <Menu
                                model={userMenuItems}
                                popup
                                ref={userMenuRef}
                            />
                            <Button
                                type="button"
                                icon="pi pi-user"
                                text
                                rounded
                                className="desktop-user-menu-button"
                                aria-label="Open user menu"
                                aria-haspopup="true"
                                onClick={(event) =>
                                    userMenuRef.current?.toggle(event)
                                }
                            />
                        </div>
                    ) : undefined
                }
            />
        </div>
    );
};


export default observer(DesktopHeader);