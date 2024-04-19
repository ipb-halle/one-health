import React from 'react';
import HeaderSearchbar from './header-searchbar.component';
import { Menubar } from 'primereact/menubar';
import { MenuItem } from 'primereact/menuitem';
import './header.component.scss';
import logo from '../../logo.svg';

const Header: React.FC = () => {
    const items: MenuItem[] = [
        {
            label: 'Home',
            icon: 'pi pi-home',
            url: '/',
        },
        {
            label: 'Ontology',
            icon: 'pi pi-sitemap',
            items: [
                {
                    label: 'Overview',
                    url: '/ontology/overview/',
                    // icon: 'pi pi-chart-bar'
                },
                {
                    label: 'New Entity Type',
                    url: '/entity-type-form',
                    // icon: 'pi pi-box'
                },
                {
                    label: 'New Link Type',
                    url: '/link-type-form',
                    // icon: 'pi pi-arrows-h'
                },
                {
                    label: 'Data Load',
                    url: '/ontology/data-load/0',
                },
            ],
        },
        {
            label: 'Search',
            icon: 'pi pi-search',
            items: [
                {
                    label: 'Explorer',
                    url: '/listing'
                },
                {
                    label: 'Co-ocurrence Search',
                    url: '/visualization/co-ocurrence-search/',
                },
                {
                    label: 'test',
                    url: '/test'
                }
            ],
        },
    ];

    const end = <HeaderSearchbar></HeaderSearchbar>;

    const start = (
        <img alt="logo" src={logo} height="40" className="mr-2"></img>
    );

    return (
        <div className="fluid fixed-top">
            <Menubar model={items} start={start} end={end} />
        </div>
    );
};

export default Header;
