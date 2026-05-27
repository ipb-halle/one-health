import React, { useContext, useState } from 'react';
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate, useLocation } from 'react-router-dom';
import { Button } from 'primereact/button';

import oneHealthLogo from '../assets/logo-n1h.png';

import './header.component.scss';
import { RootStoreContext } from '@/app/providers/store-provider';
import { observer } from 'mobx-react-lite';
import { PanelMenu } from 'primereact/panelmenu';
import { Sidebar } from 'primereact/sidebar';

const Header: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const screenDeviceStore = useContext(RootStoreContext).screenDeviceStore;

    const baseItems: MenuItem[] = [
        {
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
        },
        {
            label: 'Contribute',
            icon: 'pi pi-sitemap',
            items: [
                // {
                //     label: 'Overview',
                //     command: () => {
                //         navigate('/ontology/overview/');
                //     }
                //     // icon: 'pi pi-chart-bar'
                // },
                {
                    label: 'New Entity Type',
                    command: () => {
                        navigate('/entity-type-form');
                    },
                    // icon: 'pi pi-box'
                },
                {
                    label: 'New Link Type',
                    command: () => {
                        navigate('/link-type-form');
                    },
                    // icon: 'pi pi-arrows-h'
                },
                {
                    label: 'Data Load',
                    command: () => {
                        navigate('/ontology/data-load/0');
                    },
                },
            ],
        },
        // {
        //     label: 'Documentation',
        //     icon: 'pi pi-book',
        //     command: () => {
        //         navigate('/documentation');
        //     },
        // },
        // {
        //     label: 'Legal Information',
        //     icon: 'pi pi-exclamation-triangle',
        //     command: () => {
        //         navigate('/legal');
        //     },
        // },

        // {
        //     label: 'Contact',
        //     icon: 'pi pi-envelope',
        //     command: () => {
        //         navigate('/test');
        //     }
        // }
    ];

    const items: MenuItem[] = [...baseItems];

    if (location.pathname !== '/') {
        items.unshift({
            label: 'General Search',
            icon: 'pi pi-search',
            command: () => { screenDeviceStore.setMenuVisibility(false); navigate('/') },
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
                    height="35"
                    style={{ marginLeft: 20 }}
                    className="mr-2"
                />
            </a>
            <Button icon="pi pi-bars" text label="Menu"
                visible={screenDeviceStore.isMobile}
                onClick={() => { screenDeviceStore.setMenuVisibility(true) }} />
        </div>
    );


    const menuBar = screenDeviceStore.isMobile ? <div className="p-menubar">
        {start}
        <Sidebar visible={screenDeviceStore.mobileMenuVisible}
            onHide={() => { screenDeviceStore.setMenuVisibility(false) }} >
            <PanelMenu model={items} onClick={() => { screenDeviceStore.setMenuVisibility(false) }} />
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
