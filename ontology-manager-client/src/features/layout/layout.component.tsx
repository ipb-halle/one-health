import React from 'react';
import Header from './header.component';
import Footer from './footer.component';
import { Outlet } from 'react-router-dom';

const Layout: React.FC = () => {
    return (
        <>
            <Header></Header>
            <div className="content">
                <Outlet/>
            </div>
            <Footer></Footer>
        </>
    );
};

export default Layout;
