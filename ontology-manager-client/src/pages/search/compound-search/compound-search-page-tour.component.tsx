import Joyride, { CallBackProps, STATUS, Step } from 'react-joyride';
import React from 'react';
import compoundDrawGif from '../../../assets/tutorials/search/compound-search/compound_search_draw.gif';
import compoundImportGif from '../../../assets/tutorials/search/compound-search/compound_search_import.gif';
import compoundExactGif from '../../../assets/tutorials/search/compound-search/compound_search_exact.gif';
import compoundNeighborhoodGif from '../../../assets/tutorials/search/compound-search/compound_search_to_neighborhood.gif';

interface CompoundSearchPageTourProps {
    run: boolean;
    callback: any;
}

const CompoundSearchPageTourComponent: React.FC<CompoundSearchPageTourProps> = ({run, callback}) => {
    const handleJoyrideCallback = (data: CallBackProps) => {
        const { status, type } = data;
        const finishedStatuses: string[] = [STATUS.FINISHED, STATUS.SKIPPED];
    
        if (finishedStatuses.includes(status)) {
          callback()
        }
    
      };


    const steps: Array<Step> = [
        {
            content:'The compound search page offers multiple methods to search for compounds in the knowledge base.',
            styles: {tooltip: {width: '500px'},},
            placement: 'center',
            target: 'body',
            disableBeacon: true,
        },
        {
            target: '#compound-search-structure-panel',
            content: <div>
                The structure search panel allows you to search the database for compounds using structural methods. Just draw
                your target structure using the editor.
                <img src={compoundDrawGif} style={{width: '480px', height: '300px'}}/>
            </div>,
            styles: {tooltip: {width: '550px', height: '490px'},},
            placement: 'right',
            disableBeacon: true,
        },
        {
            target: '#compound-search-structure-panel',
            content: <div>
                You can also import your saved structures from MOL files.
                <img src={compoundImportGif} style={{width: '480px', height: '300px'}}/>
            </div>,
            styles: {tooltip: {width: '550px', height: '450px'},},
            placement: 'right',
            disableBeacon: true,
        },
        {
            target: '#compound-search-exact-panel',
            content: <div>
                Use the right panel to get exact matches by attributes.
                <img src={compoundExactGif} style={{width: '480px', height: '300px'}}/>
            </div>,
            styles: {tooltip: {width: '550px', height: '450px'},},
            placement: 'left',
            disableBeacon: true,

          // Add more options if needed
        },
        {
            target: '#compound-search-results',

            content: <div>
                You can use the obtained results as a starting point for a more in depth knowledge base exploration.
                <img src={compoundNeighborhoodGif} style={{width: '480px', height: '300px'}}/>
            </div>,
            styles: {tooltip: {width: '550px', height: '470px'},},
            placement: 'top',
            disableBeacon: true,

            // Add more options if needed
            },
            {
                target: '#page-title-help-button',
    
                content: 'You can access this tutorial at any time by clicking the help button.',
                disableBeacon: true,
    
              // Add more options if needed
            },
        // Add more steps as necessary
    ];

    return <Joyride
        callback={handleJoyrideCallback}
        steps={steps}
        run={run}
        continuous
        showProgress
        scrollToFirstStep
        showSkipButton
        
        />
}

export default CompoundSearchPageTourComponent;