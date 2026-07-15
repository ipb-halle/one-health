import React from 'react';
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate, useLocation } from 'react-router-dom';
import oneHealthLogo from '../assets/logo-n1h.png';
import './header.component.scss';

const Header: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const baseItems: MenuItem[] = [
        {
            label: 'Legal',
            icon: 'pi pi-chart-bar',
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
        }, 
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
    ];

    const items: MenuItem[] = [...baseItems];

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
            style={{ display: 'flex', alignItems: 'center', gap: '40px' }}>
            <a href="/">
                <img
                    alt="logo"
                    src={oneHealthLogo}
                    height="40"
                    style={{ marginLeft: 20 }}
                    className="mr-2"
                />
            </a>
            <p style={{ fontSize: '16px', color: '#a40', margin: 0 }}>
                This service is <strong>work in progress</strong>, layout and
                function are subject to change.
            </p>
        </div>
    );

    return (
        <div className="fluid fixed-top">
            <Menubar
                model={items}
                start={start}
                pt={{ start: { style: { marginRight: 'auto' } } }}
            />
        </div>
    );
};

export default Header;
