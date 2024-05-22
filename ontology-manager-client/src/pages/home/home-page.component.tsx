import heroSectionImage from '../../assets/img/hero-section-bg.png';
import { InputText } from 'primereact/inputtext';

const React = require('react');

const HomePageComponent: React.FC = () => {
    return (
        <div id="home-page">
            <div className='row'>
                <div className='col-6'>
                    <div style={{width: '100%', height: '100%',paddingLeft: '10%', paddingRight: '10%', paddingTop: '30%', }} >

                    <h1 style={{fontSize:"50px"}}>
                        Empowering Drug Research with Comprehensive Biochemical Data
                    </h1>
                    <p>
                        Discover our collection of biochemical data to support groundbreaking research in drug development
                    </p>
                    <div style={{width: '100%', paddingLeft: '10%', paddingRight: '10%', paddingTop: '30px'}}>

                        <div style={{ width: '90%' }}>
                            <span className="p-input-icon-left w-100">
                                <i className="pi pi-search" />
                                <InputText placeholder="Start your search here..." className="w-100" />
                            </span>
                        </div>
                    </div>
                    </div>
                </div>
                <div className='col-6'>
                    <img
                        src={heroSectionImage}
                        alt="Your SVG"
                        style={{ width: '100%', height: '925px', marginTop: '-50px' }}
                    />
                </div>
            </div>
        </div>
    );
};

export default HomePageComponent;
