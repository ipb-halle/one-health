import { ProgressSpinner } from 'primereact/progressspinner';
import React from 'react';

const LoadingPlaceholderComponent: React.FC = () => {
    // const dataTypesToIcons = DataTypes.ICONS_MAPPING();
    return (
        <div
            style={{ height: '100%' }}
            className="d-flex justify-content-center">
            <div className="h-100  d-flex align-items-center">
                <ProgressSpinner
                    style={{ width: '50px', height: '50px' }}
                    strokeWidth="8"
                    fill="var(--surface-ground)"
                    animationDuration=".5s"
                />
            </div>
        </div>
    );
};

export default LoadingPlaceholderComponent;
