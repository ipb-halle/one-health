import React from 'react';
import HeaderSearchbar from './header-searchbar.component';
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import './header.component.scss';
import logo from '../../logo.svg';
import { useNavigate } from 'react-router-dom';

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
            label: 'Ontology',
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
                    label: 'Explorer',
                    command: () => {
                        navigate('/neighborhood-explorer');
                    }
                },
                {
                    label: 'Co-ocurrence Search',
                    command: () => {
                        navigate('/visualization/co-ocurrence-search/');
                    }
                },
                {
                    label: 'test',
                    command: () => {
                        navigate('/test');
                    }
                }
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
