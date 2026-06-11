import Joyride, { CallBackProps, STATUS, Step } from 'react-joyride';
import React from 'react';
import nodeContextMenuGif from '../../../../assets/tutorials/visualization/neighborhood-explorer/node_context_menu.gif';
import nodeExpandGif from '../../../../assets/tutorials/visualization/neighborhood-explorer/node_expand.gif';
import nodeContractGif from '../../../../assets/tutorials/visualization/neighborhood-explorer/node_minimize.gif';
import nodeHideGif from '../../../../assets/tutorials/visualization/neighborhood-explorer/node_hide.gif';
import nodeLockGif from '../../../../assets/tutorials/visualization/neighborhood-explorer/node_lock.gif';
import graphSearchGif from '../../../../assets/tutorials/visualization/neighborhood-explorer/graph_search.gif';
import nodeDetailsGif from '../../../../assets/tutorials/visualization/neighborhood-explorer/node_details.gif';
import graphLoadGif from '../../../../assets/tutorials/visualization/neighborhood-explorer/graph_load.gif';
import dbSearchGif from '../../../../assets/tutorials/visualization/neighborhood-explorer/neighborhood_explorer_search.gif';

interface NeighborhoodExplorerTourProps {
    run: boolean;
    callback: any;
}

const NeighborhoodExplorerTour: React.FC<NeighborhoodExplorerTourProps> = ({
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
                'This is the neighborhood explorer that allows you to interactively explore the ontology.',
            styles: { tooltip: { width: '500px' } },
            placement: 'center',
            target: 'body',
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer-toolbar',
            content: (
                <div> In the toolbar you will find different utilities.</div>
            ),
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer-toolbar',
            content: (
                <div>
                    You can use the search bar to find specific nodes within the
                    current graph.
                    <img
                        src={graphSearchGif}
                        style={{ width: '380px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '500px', height: '460px' } },
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer-toolbar',
            content: (
                <div>
                    There are also other buttons in the toolbar:
                    <div style={{ textAlign: 'left' }}>
                        <ul>
                            <li>
                                <i className="pi pi-file" />: Creates a new
                                empty graph.
                            </li>
                            <li>
                                <i className="fa fa-camera-rotate"></i>: Resets
                                the camera to fit all the nodes in the graph.
                            </li>
                            <li>
                                <i className="pi pi-th-large"></i>: Runs the
                                layout algorithm to organize the nodes in the
                                graph.
                            </li>
                            <li>
                                <i className="pi pi-eraser"></i>: Removes all{' '}
                                <b>unlocked</b> nodes from the graph.
                            </li>
                            <li>
                                <i className="pi pi-save"></i>: Saves the
                                current graph.
                            </li>
                            <li>
                                <i className="pi pi-download"></i>: Downloads
                                the current graph in JSON or PNG format.
                            </li>
                        </ul>
                    </div>
                </div>
            ),
            styles: { tooltip: { width: '550px', height: '290px' } },
        },
        {
            target: '#neighborhood-explorer-details',
            content: (
                <div>
                    The <b>Details</b> tab on the right panel will show the
                    details of the selected node or edge.
                    <img
                        src={nodeDetailsGif}
                        style={{ width: '480px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '550px', height: '460px' } },
            placement: 'left',
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer-details',
            content: (
                <div>
                    The <b>DB Search</b> tab on the right panel allows you to
                    search in the knowledge base for new entities and add them
                    to the current graph.
                    <img
                        src={dbSearchGif}
                        style={{ width: '480px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '550px', height: '460px' } },
            placement: 'left',
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer',
            content: (
                <div>
                    In order to interact with the graph you can pop up the
                    contextual menu of a node by holding click on it
                    <img
                        src={nodeContextMenuGif}
                        style={{ width: '230px', height: '230px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '330px', height: '410px' } },
            placement: 'center',
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer',
            content: (
                <div>
                    The <b>expand</b> (<i className="fa fa-maximize" />) button
                    allows you to add to the graph all the adjacent nodes to the
                    selected one.
                    <img
                        src={nodeExpandGif}
                        style={{ width: '380px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '500px', height: '460px' } },
            placement: 'center',
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer',
            content: (
                <div>
                    The <b>contract</b> (<i className="fa fa-minimize" />)
                    button allows you hide all the adjacents nodes to the
                    selected one.
                    <img
                        src={nodeContractGif}
                        style={{ width: '380px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '500px', height: '460px' } },
            placement: 'center',
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer',
            content: (
                <div>
                    The <b>lock</b> (<i className="fa fa-lock" />) button allows
                    you lock the node in the graph preventing this from being
                    deleted until unlocked. You can also do this by double
                    clicking on the node.
                    <img
                        src={nodeLockGif}
                        style={{ width: '380px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '500px', height: '480px' } },
            placement: 'center',
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer',
            content: (
                <div>
                    The <b>hide</b> (<i className="pi pi-eye-slash" />) button
                    ... hides the node.
                    <img
                        src={nodeHideGif}
                        style={{ width: '300px', height: '240px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '400px', height: '380px' } },
            placement: 'center',
            disableBeacon: true,
        },
        {
            target: '#neighborhood-explorer-history',
            content: (
                <div>
                    You can access your saved graphs and some examples from the
                    history panel.
                    <img
                        src={graphLoadGif}
                        style={{ width: '480px', height: '300px' }}
                    />
                </div>
            ),
            styles: { tooltip: { width: '550px', height: '460px' } },
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

export default NeighborhoodExplorerTour;
