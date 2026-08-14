import logoGlacier from '../assets/logo-glacier.png';
import logoDaad from '../assets/logo-daad.png';
import logoDip from '../assets/logo-dip.png';
import logoLeibniz from '../assets/logo-leibniz.png';
import logoIpb from '../assets/logo-ipb.png';
import logoForeign from '../assets/logo-foreign-office.png';
import './footer.component.scss';

function FooterIconsMobile() {
    return (
        <div className="icons-container">
            <div className="footer-icons app-footer-coloured-dark fixed-bottom mobile-footer">
                <div className="icon"><img alt="IPB logo" src={logoIpb} /></div>
                <div className="icon"><img alt="GLACIER logo" src={logoGlacier} /></div>
                <div className="icon"><img alt="Foreign Office logo" src={logoForeign} /></div>
                <div className="icon"><img alt="DAAD logo" src={logoDaad} /></div>
                <div className="icon"><img alt="DIP logo" src={logoDip} /></div>
                <div className="icon"><img alt="Leibniz logo" src={logoLeibniz} /></div>

                <div className="icon"><img alt="IPB logo" src={logoIpb} /></div>
                <div className="icon"><img alt="GLACIER logo" src={logoGlacier} /></div>
                <div className="icon"><img alt="Foreign Office logo" src={logoForeign} /></div>
                <div className="icon"><img alt="DAAD logo" src={logoDaad} /></div>
                <div className="icon"><img alt="DIP logo" src={logoDip} /></div>
                <div className="icon"><img alt="Leibniz logo" src={logoLeibniz} /></div>
            </div>
        </div>
    );
}

export default FooterIconsMobile;
