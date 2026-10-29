import { observer } from "mobx-react-lite";
import oneHealthLogo from '@/assets/logo-n1h.png';

const DesktopStartComponent: React.FC = () => {
    return <div
        className="col"
        style={{
            display: 'flex',
            alignItems: 'center',
            gap: '40px',
            justifyContent: 'space-between',
        }}
    >
        <a href="/">
            <img
                alt="logo"
                src={oneHealthLogo}
                height="35"
                style={{ marginLeft: 20 }}
                className="mr-2"
            />
        </a>
    </div>
}


export default observer(DesktopStartComponent);