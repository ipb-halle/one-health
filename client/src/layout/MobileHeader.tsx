import { RootStoreContext } from "@/app/providers/store-provider";
import { useContext } from "react";
import { ProfileIcon } from "./ProfileIcon";
import { Sidebar } from "primereact/sidebar";
import { PanelMenu } from "primereact/panelmenu";
import oneHealthLogo from '../assets/logo-n1h.png';
import { MenuItem } from "primereact/menuitem";
import { useNavigate } from "react-router-dom";
import { observer } from "mobx-react-lite";

const MobileHeader: React.FC = () => {
    const rootStore = useContext(RootStoreContext);
    const screenDeviceStore = rootStore.screenDeviceStore;
    const navigate = useNavigate();

    const mobileMenuItems: MenuItem[] = [
        {
            label: 'Home',
            icon: 'pi pi-home',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/');
            },
        },
        {
            label: 'Documentation',
            icon: 'pi pi-book',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/documentation');
            },
        },
        {
            label: 'Legal Information',
            icon: 'pi pi-file',
            command: () => {
                screenDeviceStore.setMenuVisibility(false);
                navigate('/legal');
            },
        },
    ];

    return (
        <div className="fluid fixed-top">
            <header className="mobile-header">
                <div className="mobile-header-left">
                    <button
                        className="mobile-menu-btn"
                        onClick={() => screenDeviceStore.setMenuVisibility(true)}
                        aria-label="Toggle menu"
                    >
                        <i className="pi pi-bars" />
                    </button>
                    <a href="/" className="mobile-brand">
                        <img alt="One Health logo" src={oneHealthLogo} className="mobile-logo-img" />
                    </a>
                </div>

                <div className="mobile-header-right">
                    <ProfileIcon />
                </div>
            </header>

            <Sidebar
                visible={screenDeviceStore.mobileMenuVisible}
                onHide={() => screenDeviceStore.setMenuVisibility(false)}
            >
                <div className="mobile-sidebar-header" style={{ padding: '10px 0 15px 0', borderBottom: '1px solid #e2e8f0', marginBottom: '15px', display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <img alt="logo" src={oneHealthLogo} height="30" />
                </div>
                <PanelMenu model={mobileMenuItems} onClick={() => screenDeviceStore.setMenuVisibility(false)} />
            </Sidebar>

        </div>
    );
}

export default observer(MobileHeader);