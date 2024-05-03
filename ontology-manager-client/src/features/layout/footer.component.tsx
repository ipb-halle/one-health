
import logoGlacier from '../../resources/logo_glacier.png';
import logoDaad from '../../resources/logo_daad.png';
import logoDip from '../../resources/logo_dip.png';
import logoLeibniz from '../../resources/logo_leibniz.png';
import logoIpb from '../../resources/logo_ipb.png';
import './footer.component.scss';
 
// import {Link} from "react-router-dom";


const React = require('react');

const Footer: React.FC = () => {
    return (
   <div className='row app-footer' style={{paddingTop: '10px', paddingBottom: '10px', paddingLeft: '10px', paddingRight: '10px'}}>
        <div className='col-8'>
            <div className='row'>
                <div className='col-2'>
        <img alt="logo" src={logoIpb} height="60" style={{marginRight: '3px', marginLeft: '50px'}}></img>

                </div>
                <div className='col-8'>
        <p style={{fontSize: '8px', paddingTop: '20px'}}>
         Dolore proident ex qui. Proident voluptate consectetur ea culpa id. Elit aliqua magna veniam dolor laboris elit elit elit dolor. 
         Cillum sit velit pariatur eiusmod ad Lorem sunt exercitation sunt Cillum sit velit pariatur eiusmod ad Lorem sunt exercitation sunt
         m sit velit pariatur eiusmod ad Lorem sunt exercitation sunt Cillum sit velit pariatur eiusmod ad Lorem sunt exercitation sunt

         </p>

                </div>
            </div>
        </div>
        <div className='col-4'>
        <img alt="logo" src={logoGlacier} height="60" style={{marginRight: '3px', marginLeft: '50px'}}></img>
        <img alt="logo" src={logoDaad} height="60" style={{marginRight: '3px'}}></img>
        <img alt="logo" src={logoDip} height="40" style={{marginRight: '3px'}}></img>
        <img alt="logo" src={logoLeibniz} height="60" style={{marginRight: '3px'}}></img>
        </div>
   </div>
    );
};

export default Footer;
