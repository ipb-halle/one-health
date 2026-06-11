import logoGlacier from '../../assets/logo-glacier.png';
import logoIpb from '../../assets/logo-ipb.png';
import logoForeign from '../../assets/logo-foreign-office.png';

import './home-page.component.scss';
import React from 'react';
import HeroSectionRow from './hero-section-row.component';
import SearchPanel from '../../features/search/general-search/components/search-panel.component';
const HomePageComponent: React.FC = () => {
    return (
        <div className="page-container-narrow" id="home-page">
            <div style={{ textAlign: 'center' }}>
                <h3>
                    Empowering the research on plant-derived natural products
                    for the treatment of diseases
                </h3>
            </div>

            <SearchPanel></SearchPanel>

            <div className="row">
                <HeroSectionRow />
            </div>
            <div className="row">
                <div style={{ marginTop: '20px' }}>
                    <small style={{ textAlign: 'center' }}>
                        This project is developed and curated by
                    </small>
                    <img
                        alt="logo"
                        src={logoGlacier}
                        height="80"
                        style={{
                            marginRight: '3px',
                            marginLeft: '50px',
                        }}></img>
                    <img
                        alt="logo"
                        src={logoIpb}
                        height="80"
                        style={{
                            marginRight: '15px',
                            marginLeft: '50px',
                        }}></img>
                    <small style={{ textAlign: 'center' }}>
                        and funded by the German Federal Foreign Office
                    </small>
                    <img
                        alt="logo"
                        src={logoForeign}
                        height="100"
                        style={{
                            marginRight: '3px',
                            marginLeft: '40px',
                        }}></img>

                    <div style={{ height: '20px' }}></div>
                    <small>
                        <b>GLACIER</b> (German Latin-American Center for
                        Infection and Epidemiology Research &amp; Training) is
                        an international multidisciplinary consortium that aims
                        to level regional disparities in Latin America by
                        strengthening surveillance and response to emerging
                        infectious diseases and developing new vaccines and
                        therapies. GLACIER is made up of more than 30
                        cooperating institutions from nine countries (Germany,
                        Mexico, Cuba, Costa Rica, Nicaragua, Honduras, El
                        Salvador, Panama, and Guatemala).
                    </small>
                    <br></br>
                    <small>
                        For further information, please visit the GLACIER
                        consortium’s official website:
                        <br></br>
                        <a href="https://glacieronehealth.org/about/">
                            About | GLACIER One Health Project
                        </a>
                    </small>
                </div>
            </div>
        </div>
    );
};

export default HomePageComponent;
