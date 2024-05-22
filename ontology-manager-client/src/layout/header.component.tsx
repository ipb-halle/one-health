import React from 'react';
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate } from 'react-router-dom';
import logo from '../logo.svg';
import './header.component.scss';

const Header: React.FC = () => {

    const navigate = useNavigate();
    const items: MenuItem[] = [
        {
            label: 'Home',
            icon: 'pi pi-home',
            command: () => {
                navigate('/');
            }
        },
        {
            label: 'Search',
            icon: 'pi pi-search',
            items: [
                {
                    icon: 'fa fa-atom',
                    label: 'Compound Search',
                    command: () => {
                        navigate('/search/structure-search');
                    }
                },
                {
                    icon: 'fa fa-leaf',
                    label: 'Plant Search',
                    command: () => {
                        navigate('/search/plant-search');
                    }
                },
                {
                    icon: 'fa fa-virus',
                    label: 'Disease Search',
                    command: () => {
                        navigate('/search/disease-search');
                    }
                }
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
                    }
                },
                {
                    // icon: 'fa fa-circle-nodes',
                    label: 'Co-ocurrence Search',
                    command: () => {
                        navigate('/visualization/co-ocurrence-search/');
                    }
                }
            ],
        },
        {
            label: 'Metadata',
            icon: 'pi pi-sitemap',
            items: [
                {
                    label: 'Overview',
                    command: () => {
                        navigate('/ontology/overview/');
                    }
                    // icon: 'pi pi-chart-bar'
                },
                {
                    label: 'New Entity Type',
                    command: () => {
                        navigate('/entity-type-form');
                    }
                    // icon: 'pi pi-box'
                },
                {
                    label: 'New Link Type',
                    command: () => {
                        navigate('/link-type-form');
                    }
                    // icon: 'pi pi-arrows-h'
                },
                {
                    label: 'Data Load',
                    command: () => {
                        navigate('/ontology/data-load/0');
                    }
                },
            ],
        },  
        {
            label: 'Documentation',
            icon: 'pi pi-book',
            command: () => {
                navigate('/documentation');
            }
        },
        {
            label: 'Contact',
            icon: 'pi pi-envelope',
            command: () => {
                navigate('/contact');
            }
        }
    ];

    const start = (
        <img alt="logo" src={logo} height="40" className="mr-2"></img>
    );

    return (
        <div className="fluid fixed-top">
            <Menubar model={items} start={start} />
        </div>
    );
};

export default Header;
