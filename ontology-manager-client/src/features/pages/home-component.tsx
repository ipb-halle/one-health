import { Image } from 'primereact/image';
import homeBackground from '../../resources/Picture1.svg';
import heroSectionImage from '../../resources/img/hero-section-bg.png';
import { InputText } from 'primereact/inputtext';

const React = require('react');

const Home: React.FC = () => {
    return (
        <div>
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
            <path
                style={{ fill: 'white', width: '100px' }}
                opacity="0.33"
                d="M473,67.3c-203.9,88.3-263.1-34-320.3,0C66,119.1,0,59.7,0,59.7V0h1000v59.7 c0,0-62.1,26.1-94.9,29.3c-32.8,3.3-62.8-12.3-75.8-22.1C806,49.6,745.3,8.7,694.9,4.7S492.4,59,473,67.3z"
            ></path>
        </div>
    );
};

export default Home;
