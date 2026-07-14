import React from 'react';
import './footer.component.scss';
import FooterIcons from './footerIcons';

const Footer: React.FC = () => {
    return (
        <div className="app-footer">
            <div className="app-footer-coloured-light row">
                <ul>
                    <li>
                        <a href="/documentation">Documentation</a>
                    </li>
                    <li>
                        <a href="/imprint">Imprint</a>
                    </li>
                    <li>
                        <a href="/privacy">Privacy Policy</a>
                    </li>
                    <li>
                        <a href="/accessibility">Accessibility</a>
                    </li>
                </ul>
            </div>

        <FooterIcons />

        </div>
    );
};

export default Footer;
