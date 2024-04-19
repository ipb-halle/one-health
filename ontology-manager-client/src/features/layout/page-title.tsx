import React from 'react';

interface PageTitleProps {
    icon: string;
    title: string;
}

const PageTitle: React.FC<PageTitleProps> = ( {icon, title} ) => {
    return (
        <div className="row mb-3">
            <div className="col pt-1" style={{ maxWidth: '50px' }}>
                <i className={icon} style={{ fontSize: '32px' }}></i>
            </div>
            <div className="col">
                <h2>{title}</h2>
            </div>
        </div>
    );
};

export default PageTitle;



