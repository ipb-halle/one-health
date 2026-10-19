import { RootStoreContext } from "@/app/providers/store-provider";
import { useContext } from "react";
import { Sidebar } from "primereact/sidebar";
import { PanelMenu } from "primereact/panelmenu";
import oneHealthLogo from '@/assets/logo-n1h.png';
import { MenuItem } from "primereact/menuitem";
import { useNavigate } from "react-router-dom";
import { observer } from "mobx-react-lite";
import { createMenuItem } from "./createMenuItem";
import { ProfileIcon } from "./ProfileIcon";

const MobileHeader: React.FC = () => {
    const rootStore = useContext(RootStoreContext);
    const authStore = rootStore.authStore;
    const screenDeviceStore = rootStore.screenDeviceStore;
    const navigate = useNavigate();

    const closeMenu = () => {
        screenDeviceStore.setMenuVisibility(false);
    };

    const mobileMenuItems: MenuItem[] = [
        createMenuItem('Home', 'pi pi-home', () => {
            closeMenu();
            navigate('/');
        }),
        createMenuItem('Documentation', 'pi pi-book', () => {
            closeMenu();
            navigate('/documentation');
        }),
        createMenuItem('Legal Information', 'pi pi-file', () => {
            closeMenu();
            navigate('/legal');
        })
    ];

    if (authStore.isAuthenticated) {
        mobileMenuItems.push(
            {
                label: "Logout",
                icon: "pi pi-sign-out",
                command: async () => {
                    closeMenu();
                    void authStore.loginOrLogout(window);
                },
            },
        );
    }

    return (
        <div className="fluid fixed-top">
            <header className="mobile-header">
                <div className="mobile-header-left">
                    <button
                        type="button"
                        className="mobile-menu-btn"
                        onClick={() =>
                            screenDeviceStore.setMenuVisibility(true)
                        }
                        aria-label="Open navigation menu"
                    >
                        <i className="pi pi-bars" aria-hidden="true" />
                    </button>

                    <a href="/" className="mobile-brand">
                        <img
                            alt="One Health logo"
                            src={oneHealthLogo}
                            className="mobile-logo-img"
                        />
                    </a>
                </div>

                <div className="mobile-header-right">
                    <ProfileIcon />
                </div>
            </header>

            <Sidebar
                visible={screenDeviceStore.mobileMenuVisible}
                onHide={closeMenu}
            >
                <div
                    className="mobile-sidebar-header"
                    style={{
                        padding: '10px 0 15px 0',
                        borderBottom: '1px solid #e2e8f0',
                        marginBottom: '15px',
                        display: 'flex',
                        alignItems: 'center',
                        gap: '10px'
                    }}
                >
                    <img alt="One Health logo" src={oneHealthLogo} height="30" />
                    {authStore.isAuthenticated && (
                        <span>
                            Hi {authStore.user?.displayName}
                        </span>
                    )}
                </div>

                <PanelMenu model={mobileMenuItems} />
            </Sidebar>
        </div>
    );
}

export default observer(MobileHeader);