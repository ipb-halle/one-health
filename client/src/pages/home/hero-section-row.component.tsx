import neighborhoodExplorerImage from '../../assets/img/neighborhood-explorer.png';
import coocurrencesSummaryImage from '../../assets/img/cooccurrences-summary.png';
import contributeImage from '../../assets/img/contribute.jpeg';
import heroSectionImage from '../../assets/img/hero-section.png';

import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import { Card } from 'primereact/card';
import { Tag } from 'primereact/tag';

const HeroSectionRow: React.FC = () => {
    const navigate = useNavigate();

    const neighborhoodExplorerCardHeader = (
        <img
            alt="Neighborhood Explorer"
            src={neighborhoodExplorerImage}
            style={{ height: '100%' }}></img>
    );

    const neighborhoodExplorerCardFooter = (
        <Button
            label="Tutorial"
            onClick={() => {
                navigate('/neighborhood-explorer');
            }}></Button>
    );

    const coOccurrencesSummaryCardHeader = (
        <img
            alt="Co-Occurrences Summary"
            src={coocurrencesSummaryImage}
            style={{ height: '100%' }}></img>
    );

    const coOccurrencesSummaryCardFooter = (
        <Button
            label="Tutorial"
            onClick={() => {
                navigate('/visualization/co-occurrence-search/');
            }}></Button>
    );

    const contributeCardHeader = (
        <img
            alt="Contribute"
            src={contributeImage}
            style={{ height: '100%' }}></img>
    );

    const contributeCardFooter = (
        <Button
            label="Tutorial"
            onClick={() => {
                navigate('/ontology/data-load/0');
            }}></Button>
    );

    return (
        <div
            className="row home-card-section"
            id="hero-section"
            style={{
                marginTop: '30px', // FIXME: Right margins
                marginBottom: '30px',
                /* marginBottom: '', */
                height: '100%',
            }}>
            <div className="col-3">
                <Card
                    title="Explore"
                    footer={neighborhoodExplorerCardFooter}
                    header={neighborhoodExplorerCardHeader}
                    className="md:w-25rem">
                    {' '}
                    {/*  FIXME: Useless no Primeflex Import? */}
                    <p>
                        Explore the connections found in scientific data between
                        species, natural products and diseases
                    </p>
                </Card>
            </div>
            <div className="col-3">
                <Card
                    title="Discover"
                    footer={coOccurrencesSummaryCardFooter}
                    header={coOccurrencesSummaryCardHeader}
                    className="md:w-25rem">
                    {' '}
                    {/*  FIXME: Useless no Primeflex Import? */}
                    <p>
                        Discover co-occurrences of scientific concepts found in
                        literature and datasets
                    </p>
                </Card>
            </div>
            <div className="col-3">
                <Card
                    title="Contribute"
                    footer={contributeCardFooter}
                    header={contributeCardHeader}
                    className="md:w-25rem">
                    {' '}
                    {/*  FIXME: Useless no Primeflex Import? */}
                    <p>
                        Contribute to the platform by uploading and analyzing
                        your publications and datasets
                    </p>
                    <Tag severity="danger" value="Under development"></Tag>
                </Card>
            </div>
            <div className="col-3">
                <img
                    src={heroSectionImage}
                    alt="Earth surrounded by life and microbes"
                    style={{ width: '100%', height: '98%' }}
                />
            </div>
        </div>
    );
};

export default HeroSectionRow;
