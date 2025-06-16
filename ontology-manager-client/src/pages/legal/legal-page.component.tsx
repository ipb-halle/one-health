
import { Accordion, AccordionTab } from 'primereact/accordion';
import { PageTitle } from '../../components';
import { toolDisclaimer } from '../../utils';
        
const React = require('react');

const LegalPageComponent: React.FC = () => {
    return <div className='page-container-narrow'>
    
    <PageTitle icon='pi pi-book' title='Legal Information' help={false}></PageTitle>
    
    <Accordion multiple activeIndex={[]}>
             
    <AccordionTab header="Imprint">
        <p className="m-0">
        Imprint here.
        </p>
    </AccordionTab>
    <AccordionTab header="Privacy / GDPR Declaration">
        <p className="m-0">
        Privacy declaration here.
        </p>
    </AccordionTab>
    <AccordionTab header="Accessibility Statement">
        <p className="m-0">
        Accessibility statement here.
        </p>
    </AccordionTab>
    </Accordion>
    
    </div>;
};

export default LegalPageComponent;
