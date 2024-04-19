import { Image } from 'primereact/image';
import homeBackground from '../../resources/Picture1.svg';
import InjectionTest from '../injection/test_component';

const React = require('react');

const Home: React.FC = () => {
    return (

        <>
            
            <img
                src={homeBackground}
                alt="Your SVG"
                style={{ width: '100%', height: '700px', marginTop: '-50px' }}
            />
            <path
                style={{ fill: 'white', width: '100px' }}
                opacity="0.33"
                d="M473,67.3c-203.9,88.3-263.1-34-320.3,0C66,119.1,0,59.7,0,59.7V0h1000v59.7 c0,0-62.1,26.1-94.9,29.3c-32.8,3.3-62.8-12.3-75.8-22.1C806,49.6,745.3,8.7,694.9,4.7S492.4,59,473,67.3z"
            ></path>
        </>
    );
};

export default Home;
