import { Joyride, EventData, STATUS, Step } from 'react-joyride';
import React from 'react';
import { faL } from '@fortawesome/free-solid-svg-icons';
import myGif from '../../../assets/tutorials/visualization/neighborhood-explorer/node_context_menu.gif';

interface EntityTypeFormTourProps {
    run: boolean;
    callback: any;
}

const EntityTypeFormTour: React.FC<EntityTypeFormTourProps> = ({
    run,
    callback,
}) => {
    const handleJoyrideCallback = (data: EventData) => {
        const { status, type } = data;
        const finishedStatuses: string[] = [STATUS.FINISHED, STATUS.SKIPPED];

        if (finishedStatuses.includes(status)) {
            callback();
        }
    };

    const steps: Array<Step> = [
        {
            content: (
                <h2>
                    <img
                        src={myGif}
                        style={{ height: '500px', width: '500px' }}></img>{' '}
                    Let's begin our journey!
                </h2>
            ),
            placement: 'center',
            target: 'body',
            skipBeacon: true,
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

    return (
        <Joyride
            onEvent={handleJoyrideCallback}
            steps={steps}
            run={run}
            continuous
            scrollToFirstStep
            options={{
                showProgress: true,
                buttons: ['back', 'close', 'primary', 'skip']
            }} />
    );
};

export default EntityTypeFormTour;
