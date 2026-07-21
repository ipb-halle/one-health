import neighborhoodExplorerImage from '../../assets/img/neighborhood-explorer.png';
import coocurrencesSummaryImage from '../../assets/img/cooccurrences-summary.png';
import heroSectionImage from '../../assets/img/earth_image.png';

import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import { Card } from 'primereact/card';

const HeroSectionRow: React.FC = () => {
    const navigate = useNavigate();

    const neighborhoodExplorerCardHeader = (
        <img
            alt="Neighborhood Explorer"
            src={neighborhoodExplorerImage} />
    );

    const neighborhoodExplorerCardFooter = (
        <Button
            label="Try it"
            onClick={() => {
                navigate('/neighborhood-explorer');
            }} />
    );

    const coOccurrencesSummaryCardHeader = (
        <img
            alt="Co-Occurrences Summary"
            src={coocurrencesSummaryImage} />
    );

    const coOccurrencesSummaryCardFooter = (
        <Button
            label="Try it"
            onClick={() => {
                navigate('/visualization/co-occurrence-search/');
            }} />
    );

    const heroSectionHeader = (
        <img
            alt="Earth surrounded by life and microbes"
            src={heroSectionImage} />
    );

    return (
        <div
            className="row home-card-section"
            id="hero-section">
            <div className="col-4">
                <Card
                    title="Explore"
                    footer={neighborhoodExplorerCardFooter}
                    header={neighborhoodExplorerCardHeader}
                    className="md:w-25rem">
                    <p>
                        Explore the connections found in scientific data between
                        species, natural products and diseases
                    </p>
                </Card>
            </div>
            <div className="col-4">
                <Card
                    title="Discover"
                    footer={coOccurrencesSummaryCardFooter}
                    header={coOccurrencesSummaryCardHeader}
                    className="md:w-25rem">
                    <p>
                        Discover co-occurrences of scientific concepts found in
                        literature and datasets
                    </p>
                </Card>
            </div>
            <div className="col-4">
                <Card
                    title="Investigate"
                    footer={' '}
                    header={heroSectionHeader}
                    className="md:w-25rem">
                    <p>
                        Uncover previously unknown relationships.
                    </p>
                </Card>
            </div>
        </div>
    );
};

export default HeroSectionRow;
