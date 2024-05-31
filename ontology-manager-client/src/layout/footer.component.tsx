
import React from 'react';
import logoGlacier from '../assets/logo_glacier.png';
import logoDaad from '../assets/logo_daad.png';
import logoDip from '../assets/logo_dip.png';
import logoLeibniz from '../assets/logo_leibniz.png';
import logoIpb from '../assets/logo_ipb.png';
import './footer.component.scss';

const Footer: React.FC = () => {
    return (
        <div className='row app-footer col-12' style={{paddingTop: '10px', paddingBottom: '10px', paddingLeft: '10px', paddingRight: '10px'}}>
            <div className='col-8'>
                <div className='row'>
                    <div className='col-2'>
                        <img alt="logo" src={logoIpb} height="60" style={{marginRight: '3px', marginLeft: '50px'}}></img>
                    </div>
                    <div className='col-8'>
                        <p style={{fontSize: '8px', paddingTop: '20px'}}>
                            The Natural-One-Health service (n1h.org) is developed and operated by the Leibniz Institute of Plant Biochemistry. See
                            the <a href="https://www.ipb-halle.de/kontakt/impressum">imprint</a> for legal details.
                        </p>
                    </div>
                </div>
            </div>
            <div className='col-4'>
                <img alt="logo" src={logoGlacier} height="60" style={{marginRight: '3px', marginLeft: '50px'}}></img>
                <img alt="logo" src={logoDaad} height="60" style={{marginRight: '3px'}}></img>
                <img alt="logo" src={logoDip} height="40" style={{marginRight: '3px'}}></img>
                <img alt="logo" src={logoLeibniz} height="60" style={{marginRight: '3px'}}></img>
            </div>
        </div>
    );
};

export default Footer;
