import React, { ReactComponentElement, ReactElement } from 'react';
import { Card } from 'primereact/card';

import './overview-card.component.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IconDefinition, faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { Button } from 'primereact/button';

interface OverviewCardProps {
    leftCount: number;
    leftLabel: string;
    rightCount: number;
    rightLabel: string;
    icon: IconDefinition;
    actions: ReactElement;
}

const OverviewCard: React.FC<OverviewCardProps> = ({
    leftCount,
    leftLabel,
    rightCount,
    rightLabel,
    icon,
    actions,
}) => {
    const header = (
        <img
            alt="Card"
            src="https://primefaces.org/cdn/primereact/images/usercard.png"
        />
    );

    const footer = (
        <div className="d-flex justify-content-center">{actions}</div>
    );

    return (
        <>
            <Card footer={footer} className="card">
                <div className="row d-flex justify-content-start mb-2">
                    <div className="col-sm-4 d-flex icon-container align-items-center">
                        <FontAwesomeIcon
                            icon={icon}
                            size="lg"
                            color="#747474"
                        />
                    </div>
                </div>
                <div className="row">
                    <div
                        className="col-sm-6"
                        style={{
                            borderRight: '1px solid #dee2e6',
                            textAlign: 'center',
                        }}
                    >
                        <h3>{leftCount}</h3>
                        {leftLabel}
                    </div>
                    <div className="col-sm-6" style={{ textAlign: 'center' }}>
                        <h3>{rightCount}</h3>
                        {rightLabel}
                    </div>
                </div>
            </Card>
        </>
    );
};

export default OverviewCard;
