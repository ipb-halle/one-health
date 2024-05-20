import { InputText } from 'primereact/inputtext';
import './header-searchbar.component.scss';

const React = require('react');

const HeaderSearchbar: React.FC = () => {
    return (
        <div className="px-4" style={{ width: '500px' }}>
            <span className="p-input-icon-left w-100">
                <i className="pi pi-search" />
                <InputText placeholder="Search" className="w-100" />
            </span>
        </div>
    );
};

export default HeaderSearchbar;
