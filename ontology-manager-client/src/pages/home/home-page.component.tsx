import heroSectionImage from '../../assets/img/hero-section-bg.png';
import { InputText } from 'primereact/inputtext';
import logoGlacier from '../../assets/logo_glacier.png';
import logoIpb from '../../assets/logo_ipb.png';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import neighborhoodExplorerImage from "../../assets/img/neighborhood-explorer.png";
import coocurrencesSummaryImage from "../../assets/img/cooccurrences-summary.png";
import contributeImage from "../../assets/img/contribute.jpeg";
import { useNavigate } from 'react-router-dom';
import { Message } from 'primereact/message';
import './home-page.component.scss';
import { Tag } from 'primereact/tag';

const React = require('react');

const HomePageComponent: React.FC = () => {
    const navigate = useNavigate();


    const neighborhoodExplorerCardHeader = (
        <img alt="Neighborhood Explorer" src={neighborhoodExplorerImage} style={{height: "100%"}}></img>
    );

    const coOccurrencesSummaryCardHeader = (
        <img alt="Co-Occurrences Summary" src={coocurrencesSummaryImage} style={{height: "100%"}}></img>
    );

    const contributeCardHeader = (
        <img alt="Contribute" src={contributeImage} style={{height: "100%"}}></img>
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

    const contributeCardFooter = (
        <>
            <Button label='Tutorial' onClick={() => {navigate('/ontology/data-load/0');}}></Button>
        </>
    )

    return (
        <div id="home-page">
            <div className='row' id='hero-section'>
                <div className='col-6'>
                    <div style={{width: '100%', height: '100%',paddingLeft: '10%', paddingRight: '10%', }} >

                    <h2 style={{textAlign: 'center'}}>
Empowering drug development with integrated biochemical data
</h2>
A platform for biochemical data exchange and virtual repository to empower drug development
                    

                    <div className='row home-card-section' style={{marginTop: "30px"}} >
 

                        <div className='col-4'>
                            <Card title="Explore" footer={neighborhoodExplorerCardFooter} header={neighborhoodExplorerCardHeader} className="md:w-25rem">
                                <p>
                                Explore the connections found in scientific data between species, natural products and diseases
                                </p>
                            </Card>
                        </div>
                        <div className='col-4'>
                        <Card title="Discover" footer={coOccurrencesSummaryCardFooter} header={coOccurrencesSummaryCardHeader} className="md:w-25rem">
                                <p>
                                    Discover co-occurrences of scientific concepts found in literature and datasets
                                </p>
                            </Card>
                        </div>
                        <div className='col-4'>
                        <Card title="Contribute" footer={contributeCardFooter} header={contributeCardHeader} className="md:w-25rem">
                                <p>
                                   Contribute to the platform by uploading and analyzing your publications and datasets
                                </p>
                                <Tag severity="danger" value="Under development"></Tag>
                            </Card>
                        </div>

                    </div>


                    <div style={{height: '20px', marginTop: '10px'}}>

                    </div>
                 
                    <small style={{textAlign: 'center'}}>developed and curated by</small>
                    <img alt="logo" src={logoGlacier} height="80" style={{marginRight: '3px', marginLeft: '50px'}}></img>
                    <img alt="logo" src={logoIpb} height="80" style={{marginRight: '3px', marginLeft: '50px'}}></img>

                    
                    <div style={{height: "20px"}}></div>
                    <small>
                    <b>GLACIER</b> (German Latin-American Center for Infection and Epidemiology Research &amp; Training) is an international multidisciplinary consortium that aims to level regional disparities in Latin America by strengthening surveillance and response to emerging infectious diseases and developing new vaccines and therapies. GLACIER is made up of more than 30 cooperating institutions from nine countries (Germany, Mexico, Cuba, Costa Rica, Nicaragua, Honduras, El Salvador, Panama, and Guatemala).
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
