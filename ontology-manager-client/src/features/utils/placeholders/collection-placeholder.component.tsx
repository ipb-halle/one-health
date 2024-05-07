import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faQuoteRight,
    faSquareCheck,
    faListNumeric,
    faCalendar,
    faList,
} from '@fortawesome/free-solid-svg-icons';

const React = require('react');


interface CollectionPlaceholderProps {
    icon: string;
    message: string;
}

const CollectionPlaceholder: React.FC<CollectionPlaceholderProps> = ({icon, message}) => {
    // const dataTypesToIcons = DataTypes.ICONS_MAPPING();
    return <div style={{height: '100%'}} className="d-flex justify-content-center">
                <div className="h-100  d-flex align-items-center">
                    <div className="row" style={{ width: '150px' }}>
                        <i
                            className={icon}
                            style={{
                                fontSize: '120px',
                                color: '#F8F9FA',
                                height: '120px',
                                WebkitTextStroke: "1px #DEE2E6",
                            }}>
                        </i>
                        <span>{message}</span>
                    </div>
                </div>
            </div>
    
};

export default CollectionPlaceholder;
