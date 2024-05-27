import Joyride, { CallBackProps, STATUS, Step } from 'react-joyride';
import React from 'react';
import queryGif from '../../../assets/tutorials/search/general-search/general_search_examples.gif';
import toNeighborhoodGif from '../../../assets/tutorials/search/general-search/general_search_neighbordhood.gif';
import historyGif from '../../../assets/tutorials/search/general-search/general_search_history.gif';

interface GeneralSearchPageTourProps {
    run: boolean;
    callback: any;
}

const GeneralSearchPageTourComponent: React.FC<GeneralSearchPageTourProps> = ({run, callback}) => {
    const handleJoyrideCallback = (data: CallBackProps) => {
        const { status, type } = data;
        const finishedStatuses: string[] = [STATUS.FINISHED, STATUS.SKIPPED];
    
        if (finishedStatuses.includes(status)) {
          callback()
        }
    
      };


    const steps: Array<Step> = [
        {
            content:'The general search page allows you query the entire knowledge base from a single place.',
            styles: {tooltip: {width: '500px'},},
            placement: 'center',
            target: 'body',
            disableBeacon: true,
        },
        {
            target: '#general-search-header',
            content: <div>
                You can use the search bar to search for names, synonyms and attributes in the knowledge base.
                <img src={queryGif} style={{width: '480px', height: '300px'}}/>
            </div>,
            styles: {tooltip: {width: '550px', height: '460px'},},
            placement: 'bottom',
            disableBeacon: true,
        },
        {
            target: '#general-search-panel',
            content: <div>
                You can use the obtained results as a starting point to continue exploring the knowledge base.
                <img src={toNeighborhoodGif} style={{width: '480px', height: '300px'}}/>
            </div>,
            styles: {tooltip: {width: '550px', height: '460px'},},
            placement: 'center',
            disableBeacon: true,
        },
        {
            target: '#general-search-history',
            content: <div>
                You can load your previous queries by using the left panel.
                <img src={historyGif} style={{width: '480px', height: '300px'}}/>
            </div>,
            styles: {tooltip: {width: '550px', height: '460px'},},
            placement: 'right',
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

export default GeneralSearchPageTourComponent;