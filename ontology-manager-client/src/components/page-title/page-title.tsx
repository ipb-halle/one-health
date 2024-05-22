import React from 'react';

interface PageTitleProps {
    icon: string;
    title: string;
    help: boolean;
    helpClickedHandler?: any;
}

const PageTitle: React.FC<PageTitleProps> = ( {icon, title, help, helpClickedHandler} ) => {
    return (
        <div className="row mb-3">
            <div className="col pt-1" style={{ maxWidth: '50px' }}>
                <i className={icon} style={{ fontSize: '28px' }}></i>
            </div>
            <div className="col">
                <h3>{title}</h3>
            </div>
            <div hidden={!help} className='col' style={{textAlign: 'right'}}>
                <i className='pi pi-question-circle' style={{ fontSize: '28px', marginTop: '5px', color:"gray" }} onClick={helpClickedHandler}></i>
                
            </div>
        </div>
    );
};

export default PageTitle;



