import "reflect-metadata";
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './features/layout/app.component';
import reportWebVitals from './reportWebVitals';
import { PrimeReactProvider } from 'primereact/api';
import axios from "axios";
import { ToastMessageServiceProvider } from "./features/messages";
import { BrowserRouter } from 'react-router-dom';

axios.defaults.baseURL = process.env.REACT_APP_API_URL;

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement,
);
root.render(
    <React.StrictMode>
        <PrimeReactProvider>
            <ToastMessageServiceProvider>
                <BrowserRouter>
                    <App />
                </BrowserRouter>
            </ToastMessageServiceProvider>
        </PrimeReactProvider>
    </React.StrictMode>,
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
