import { DataTypes } from '../data-types/data-types';
import { IProperty } from '../properties';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faQuoteRight,
    faSquareCheck,
    faListNumeric,
    faCalendar,
    faList,
} from '@fortawesome/free-solid-svg-icons';
import { IMetadataElement } from './metadata-element';
import { Tooltip } from 'primereact/tooltip';
import { Link } from 'react-router-dom';
import { Button } from 'primereact/button';
import { CollectionPlaceholderComponent } from '../../../../components';

const React = require('react');

interface ElementViewProps {
    element: IMetadataElement;
}

const ElementView: React.FC<ElementViewProps> = ({element
}) => {
    const dataTypesToIcons = DataTypes.ICONS_MAPPING();

    if (element.id)
    return (
        
        <>
            <div
                className="row"
                style={{
                    width: '100%',
                    height: '70px',
                    margin: 0,
                    borderBottom: '1px solid #dee2e6',
                }}
            >
                <div className="col-sm-2">
                    <div
                        style={{
                            width: '50px',
                            height: '50px',
                            marginTop: '10px',
                            backgroundColor: 'black',
                        }}
                    ></div>
                </div>

                <div className="col-sm-8" style={{ paddingTop: '5px' }}>
                    <h5 style={{ marginLeft: '5px' }}>{element.name}</h5>
                    <label style={{ marginLeft: '5px' }}>
                        {element.count} objects
                    </label>
                </div>

                <div className='col-sm-2' style={{paddingTop:"15px"}}>

                    {element.type === "entity" && 
                    <Link to={`/entity-type-form/${element.id}`}>
                        <Button  icon="pi pi-pencil" rounded text></Button>
                    </Link>
                    }
                      {element.type === "link" && 
                    <Link to={`/link-type-form/${element.id}`}>
                        <Button  icon="pi pi-pencil" rounded text></Button>
                    </Link>
                    }

                </div>
            </div>

            <div
                className="py-2 px-3"
                style={{
                    borderBottom: '1px solid #dee2e6',
                    margin: 0,
                    height: '100px',
                    overflowY: 'scroll',
                }}
            >
                {element.description}
            </div>

            <div
                className="d-flex py-2 px-3"
                style={{ borderBottom: '1px solid #dee2e6', margin: 0 }}
            >
                {/* <i className="pi pi-list" style={{fontSize: "25px", marginRight:"10px"}}></i> */}
                <FontAwesomeIcon icon={faList} size="lg"></FontAwesomeIcon>
                <label style={{ marginLeft: '10px' }}>Properties {element.properties?.length}</label>
            </div>

            <div style={{ height: '389px', overflow: 'scroll' }}>

                {element.properties.length > 0 && element.properties.map((x) => (
                    <div
                        className="d-flex py-2 px-3"
                        style={{ borderBottom: '1px solid #dee2e6', margin: 0 }}
                    >
                        <FontAwesomeIcon icon={dataTypesToIcons[x.dataType]} size='lg'></FontAwesomeIcon>

                        <label style={{ marginLeft: '10px', marginRight: '3px'}}>{x.name}</label>

                        {
                            x.description && 
                            <>
                            <Tooltip target=".custom-target-icon" />

                            <i className="custom-target-icon pi pi-question-circle p-text-secondary p-overlay-badge"
                                data-pr-tooltip={x.description}
                                data-pr-position="right"
                                data-pr-at="right+5 top"
                                data-pr-my="left center-2"
                                style={{ fontSize: '1rem', cursor: 'pointer' }}>
                            </i>
                        </>
                        }
                    </div>
                ))}
            </div>
        </>
    ); 
    else return (
        <CollectionPlaceholderComponent icon='pi pi-list' message=''/>
    );
};

export default ElementView;
