import React, { useContext } from 'react';
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate, useLocation } from 'react-router-dom';
import oneHealthLogo from '../assets/logo-n1h.png';
import './header.component.scss';
import { RootStoreContext } from '@/app/providers/store-provider';
import { observer } from 'mobx-react-lite';
import { PanelMenu } from 'primereact/panelmenu';
import { Sidebar } from 'primereact/sidebar';
import { Button } from 'primereact/button';

const Header: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const legalItems: MenuItem =
    {
        label: 'Legal',
        icon: 'pi pi-file',
        items: [
            {
                // icon: 'pi pi-compass',
                label: 'Documentation',
                command: () => {
                    navigate('/documentation');
                },
            },
            {
                // icon: 'fa pi-exclamation-triangle',
                label: 'Imprint',
                command: () => {
                    navigate('/imprint/');
                },
            },
            {
                // icon: 'fa fa-circle-nodes',
                label: 'Privacy Policy',
                command: () => {
                    navigate('/privacy/');
                },
            },
            {
                // icon: 'fa fa-circle-nodes',
                label: 'Accessibility',
                command: () => {
                    navigate('/accessibility/');
                },
            },
        ]
    };


    const visItems: MenuItem = {
        label: 'Visualization',
        icon: 'pi pi-chart-bar',
        items: [
            {
                // icon: 'pi pi-compass',
                label: 'Neighborhood Explorer',
                command: () => {
                    navigate('/neighborhood-explorer');
                },
            },
            {
                // icon: 'fa fa-circle-nodes',
                label: 'Co-occurrences Search',
                command: () => {
                    navigate('/visualization/co-occurrence-search/');
                },
            },
        ],
    };

    const items: MenuItem[] = [legalItems];
    const screenDeviceStore = useContext(RootStoreContext).screenDeviceStore;

    if (!screenDeviceStore.isMobile) {
        items.push(visItems);
    }

    if (location.pathname !== '/') {
        items.unshift({
            label: 'General Search',
            icon: 'pi pi-search',
            command: () => navigate('/'),
        });
    }

    const start = (
        <div
            className="col"
            style={{ display: 'flex', alignItems: 'center', gap: '40px', justifyContent: 'space-between' }}>
            <a href="/">
                <img
                    alt="logo"
                    src={oneHealthLogo}
                    height="40"
                    style={{ marginLeft: 20 }}
                    className="mr-2"
                />
            </a>
            <Button icon="pi pi-file" text label="Menu"
                visible={screenDeviceStore.isMobile}
                onClick={() => { screenDeviceStore.setMenuVisibility(true) }} />
        </div>
    );


    const menuBar = screenDeviceStore.isMobile ? <div>
        {start}
        <Sidebar visible={screenDeviceStore.mobileMenuVisible}
            onHide={() => { screenDeviceStore.setMenuVisibility(false) }} >
            <PanelMenu model={items} />
        </Sidebar>
    </div>
        : <Menubar
            model={items}
            start={start}
            pt={{ start: { style: { marginRight: 'auto' } } }}
        />

    return (
        <div className="fluid fixed-top">
            {/* <Menubar
                model={items}
                start={start}
                pt={{ start: { style: { marginRight: 'auto' } } }}
            /> */}
            {menuBar}
        </div>
    );
};


export default observer(Header);
