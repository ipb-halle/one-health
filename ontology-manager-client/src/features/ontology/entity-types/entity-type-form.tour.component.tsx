import Joyride, { CallBackProps, STATUS, Step } from 'react-joyride';
import React from 'react';
import { faL } from '@fortawesome/free-solid-svg-icons';

interface EntityTypeFormTourProps {
    run: boolean;
    callback: any;
}

const EntityTypeFormTour: React.FC<EntityTypeFormTourProps> = ({run, callback}) => {
    const handleJoyrideCallback = (data: CallBackProps) => {
        const { status, type } = data;
        const finishedStatuses: string[] = [STATUS.FINISHED, STATUS.SKIPPED];
    
        if (finishedStatuses.includes(status)) {
          callback()
        }
    
      };


    const steps: Array<Step> = [
        {
            content: <h2>Let's begin our journey!</h2>,
            placement: 'center',
            target: 'body',
            disableBeacon: true
        },
        {
          target: '#definition',
          content: 'This is the first step of the tour',
          // Add more options if needed
        },
        {
          target: '#properties',
          content: 'This is the second step of the tour',
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

export default EntityTypeFormTour;