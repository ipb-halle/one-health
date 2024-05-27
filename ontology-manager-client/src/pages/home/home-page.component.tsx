import heroSectionImage from '../../assets/img/hero-section-bg.png';
import { InputText } from 'primereact/inputtext';
import logoGlacier from '../../assets/logo_glacier.png';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import neighborhoodExplorerImage from "../../assets/img/neighborhood-explorer.png";
import coocurrencesSummaryImage from "../../assets/img/cooccurrences-summary.png";
import { useNavigate } from 'react-router-dom';

const React = require('react');

const HomePageComponent: React.FC = () => {
    const navigate = useNavigate();


    const neighborhoodExplorerCardHeader = (
        <img alt="Neighborhood Explorer" src={neighborhoodExplorerImage}></img>
    );

    const coOccurrencesSummaryCardHeader = (
        <img alt="Co-Occurrences Summary" src={coocurrencesSummaryImage}></img>
    );

    const neighborhoodExplorerCardFooter = (
        <>
            <Button label='Tutorial' onClick={() => {navigate('/neighborhood-explorer');}}></Button>
        </>
    )

    const coOccurrencesSummaryCardFooter = (
        <>
        <Button label='Tutorial' onClick={() => {navigate('/visualization/co-ocurrence-search/');}}></Button>
        </>
    )

    return (
        <div id="home-page">
            <div className='row' id='hero-section'>
                <div className='col-6'>
                    <div style={{width: '100%', height: '100%',paddingLeft: '10%', paddingRight: '10%', }} >

                    <h2 style={{fontSize:"35px", textAlign: 'center'}}>
                        Empowering Drug Research with Comprehensive Biochemical Data
                    </h2>
                    <p>
                        Discover our collection of biochemical data to support groundbreaking research in drug development
                    </p>

                    <div className='row'>
                        <div className='col-2'>

                        </div>

                        <div className='col-4'>
                            <Card footer={neighborhoodExplorerCardFooter} header={neighborhoodExplorerCardHeader} className="md:w-25rem">
                                <p>
                                Explore the ontology without missing any details with our interactive explorer.
                                </p>
                            </Card>
                        </div>
                        <div className='col-4'>
                        <Card footer={coOccurrencesSummaryCardFooter} header={coOccurrencesSummaryCardHeader} className="md:w-25rem">
                                <p>
                                Review the connections found in scientific literature between entities of your interest.
                                </p>
                            </Card>
                        </div>
                        <div className='col-2'>

                        </div>

                    </div>


                    <div style={{height: '20px'}}>

                    </div>
                    <h4 style={{textAlign: 'center'}}>

                    A data exchange platform and virtual repository for emerging infectious disease surveillance
                    </h4>
                    <small style={{textAlign: 'center'}}>developed and curated by</small>
                    <img alt="logo" src={logoGlacier} height="80" style={{marginRight: '3px', marginLeft: '50px'}}></img>
                    
                    <div style={{height: "20px"}}></div>
                    <small>
                    <b>GLACIER</b> (German Latin-American Center for Infection and Epidemiology Research) is an international multidisciplinary consortium that aims to level regional disparities in Latin America by strengthening surveillance and response to emerging infectious diseases and developing new vaccines and therapies. GLACIER is made up of more than 30 cooperating institutions from nine countries (Germany, Mexico, Cuba, Costa Rica, Nicaragua, Honduras, El Salvador, Panama, and Guatemala).
For further information, please visit the GLACIER consortium’s official website <a href='https://glacieronehealth.org/about/'>About | GLACIER One Health Project</a>

                    </small>
                    </div>
                </div>
                <div className='col-6'>
                    <img
                        src={heroSectionImage}
                        alt="Your SVG"
                        style={{ width: '100%', height: '925px', marginTop: '-35px' }}
                    />
                </div>
            </div>
        </div>
    );
};

export default HomePageComponent;
