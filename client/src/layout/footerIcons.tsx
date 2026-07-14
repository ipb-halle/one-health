import logoGlacier from '../assets/logo-glacier.png';
import logoDaad from '../assets/logo-daad.png';
import logoDip from '../assets/logo-dip.png';
import logoLeibniz from '../assets/logo-leibniz.png';
import logoIpb from '../assets/logo-ipb.png';
import logoForeign from '../assets/logo-foreign-office.png';
import './footer.component.scss';

function FooterIcons() {
    return             <div
                className="app-footer-coloured-dark row fixed-bottom"
                style={{ padding: '0px' }}>
                <div className="col-7">
                    <div className="col-2">
                        <img
                            alt="logo"
                            src={logoIpb}
                            height="60"
                            style={{ marginRight: '3px' }}></img>
                    </div>
                </div>
                <div
                    className="col-5"
                    style={{
                        display: 'flex',
                        justifyContent: 'end',
                        alignItems: 'center',
                    }}>
                    <img
                        alt="logo"
                        src={logoGlacier}
                        height="60"
                        style={{ marginRight: '3px' }}></img>
                    <img
                        alt="logo"
                        src={logoForeign}
                        height="80"
                        style={{ marginRight: '3px' }}></img>
                    <img
                        alt="logo"
                        src={logoDaad}
                        height="80"
                        style={{ marginRight: '3px' }}></img>
                    <img
                        alt="logo"
                        src={logoDip}
                        height="40"
                        style={{ marginRight: '3px' }}></img>
                    <img
                        alt="logo"
                        src={logoLeibniz}
                        height="60"
                        style={{ marginRight: '3px' }}></img>
                </div>
            </div>
}

export default FooterIcons;