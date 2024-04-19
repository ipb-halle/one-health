import React from 'react';
import logo from './logo.svg';
import './App.css';

import Header from './features/layout/header.component';
import Footer from './features/layout/footer.component';
import { Container } from 'react-bootstrap';
import './Style.scss';

// import "bootstrap/scss/bootstrap.scss";
function App() {
    return (
        <div className="App">
            <Container fluid className="content" id="contentContainer">
                Hello there my friend sjdflksdjlf
            </Container>
            <Footer></Footer>
        </div>
    );
}

export default App;
