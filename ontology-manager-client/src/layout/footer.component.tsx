
import React, { useEffect, useState } from 'react';
import logoGlacier from '../assets/logo_glacier.png';
import logoDaad from '../assets/logo_daad.png';
import logoDip from '../assets/logo_dip.png';
import logoLeibniz from '../assets/logo_leibniz.png';
import logoIpb from '../assets/logo_ipb.png';
import logoForeign from "../assets/logo_foreign_office.png";
import './footer.component.scss';

interface ImageSliderProps {
    images: any
}

const ImageSlider: React.FC<ImageSliderProps> = ({images}) => { // Array of image paths
    const [currentIndex, setCurrentIndex] = useState<number>(0);
  
    useEffect(() => {
      const interval = setInterval(() => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
      }, 5000); // Change image every 5 seconds
  
      return () => clearInterval(interval); // Cleanup the interval on component unmount
    }, []);
  
    return (
       <>{images[currentIndex]}</>
    );
  };


const Footer: React.FC = () => {
    return (
        <div className='row app-footer'>
            <div className='col-lg-7'>
                    <div className='row' style={{alignItems:"center", height:"100%"}}>
                        <div className='col-4 col-lg-2'>

                            {window.innerWidth > 1024 && 
                            <img alt="logo" src={logoIpb} height="60" style={{marginRight: '3px'}}></img>}

                            {window.innerWidth < 1024 && 
                                <ImageSlider images={[
                                    <img alt="logo" src={logoIpb}  style={{marginRight: '3px', height:"3rem"}}></img>,
                                    <img alt="logo" src={logoGlacier}  style={{marginRight: '3px', height:"3rem"}}></img>,
                                    <img alt="logo" src={logoForeign}  style={{marginRight: '3px', height:"4rem"}}></img>,
                                    <img alt="logo" src={logoDaad}  style={{marginRight: '3px', height:"3rem"}}></img>,
                                    <img alt="logo" src={logoDip} style={{marginRight: '3px', height:"2rem"}}></img>,
                                    <img alt="logo" src={logoLeibniz}  style={{marginRight: '3px', height:"3rem"}}></img>
                                ]}></ImageSlider>
                            }                       
                        </div>

                        <div className='col-8 col-lg-10'>
                            <p style={{fontSize: '8px', paddingTop: '20px'}}>
                                The Natural-One-Health service (n1h.org) is developed and operated by the Leibniz Institute of Plant Biochemistry. See
                                the <a href="https://www.ipb-halle.de/kontakt/impressum" target='_blank'>imprint</a> for legal details.
                            </p>
                        </div>

                    </div>

                        
            </div>
            <div className='col-lg-5 app-footer-partners'>
                <img alt="logo" src={logoGlacier}  style={{marginRight: '3px', marginLeft: 'auto', height:"3rem"}}></img>
                <img alt="logo" src={logoForeign} style={{marginRight: '3px', height:"4rem"}}></img>
                <img alt="logo" src={logoDaad}  style={{marginRight: '3px', height:"3rem"}}></img>
                <img alt="logo" src={logoDip}  style={{marginRight: '3px', height:"2.5rem"}}></img>
                <img alt="logo" src={logoLeibniz} style={{marginRight: '3px',  height:"3rem"}}></img>
            </div>
        </div>
    );
};

export default Footer;
