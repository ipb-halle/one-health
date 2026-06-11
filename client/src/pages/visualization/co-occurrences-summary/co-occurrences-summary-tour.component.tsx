import Joyride, { CallBackProps, STATUS, Step } from 'react-joyride';
import React from 'react';
import coOcurrencesFiltersGif from '../../../assets/tutorials/visualization/co-occurrences-summary/coocurrences_filter.gif';
import coOcurrencesLoadGif from '../../../assets/tutorials/visualization/co-occurrences-summary/cooccurrences_load.gif';
import coOcurrencesDetailsGif from '../../../assets/tutorials/visualization/co-occurrences-summary/coocurrences_details.gif';

interface CoOccurrencesSummaryTourProps {
    run: boolean;
    callback: any;
}

const CoOccurrencesSummaryTour: React.FC<CoOccurrencesSummaryTourProps> = ({
    run,
    callback,
}) => {
    const handleJoyrideCallback = (data: CallBackProps) => {
        const { status, type } = data;
        const finishedStatuses: string[] = [STATUS.FINISHED, STATUS.SKIPPED];

        if (finishedStatuses.includes(status)) {
            callback();
        }
    };

    const steps: Array<Step> = [
        {
            content:
                'The Co-Occurrences summary allows you to aggregate the co-occurrences in the scientific literature between two entities or entity groups. The width of the link between two entities in the chart represents the amount of co-occurrences found in the data.',
            styles: { tooltip: { width: '500px' } },
            placement: 'center',
            target: 'body',
            disableBeacon: true,
        },
        {
            target: '#cooccurrences-summary-toolbar',
            content: (
                <div>
                    In the toolbar you will find different utilities:
                    <div style={{ textAlign: 'left' }}>
                        <ul>
                            <li>
                                <i className="pi pi-file" />: Creates a new
                                empty chart.
                            </li>
                            <li>
                                <i className="pi pi-filter"></i>: Runs the query
                                with the specified filters.
                            </li>
                            <li>
                                <i className="pi pi-save"></i>: Saves the
                                current chart and filters.
                            </li>
                            <li>
                                <i className="pi pi-download"></i>: Downloads
                                the current chart in JSON or PNG format.
                            </li>
                        </ul>
                    </div>
                </div>
            ),
            styles: { tooltip: { width: '550px', height: '250px' } },
            disableBeacon: true,
        },
        {
            target: '#cooccurrences-summary-filters',
            content: (
                <div>
                    The filters panel allows you to specify the query
                    <img
                        src={coOcurrencesFiltersGif}
                        style={{ width: '480px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '550px', height: '450px' } },
            placement: 'left',
            disableBeacon: true,
        },
        {
            target: '#cooccurrences-summary-details',
            content: (
                <div>
                    The details panel will show the details of every
                    co-occurrence between the selected entities (groups of
                    entities).
                    <img
                        src={coOcurrencesDetailsGif}
                        style={{ width: '480px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '550px', height: '470px' } },
            placement: 'top',
            disableBeacon: true,
        },
        {
            target: '#cooccurrences-summary-history',
            content: (
                <div>
                    You can access your saved charts and some examples from the
                    history panel.
                    <img
                        src={coOcurrencesLoadGif}
                        style={{ width: '480px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '550px', height: '470px' } },
            placement: 'right',
            disableBeacon: true,

            // Add more options if needed
        },
        {
            target: '#page-title-help-button',

            content:
                'You can access this tutorial any time by clicking the help button.',
            disableBeacon: true,

            // Add more options if needed
        },
        // Add more steps as necessary
    ];

    return (
        <Joyride
            callback={handleJoyrideCallback}
            steps={steps}
            run={run}
            continuous
            showProgress
            scrollToFirstStep
            showSkipButton
        />
    );
};

export default CoOccurrencesSummaryTour;
