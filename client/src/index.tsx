import 'reflect-metadata';
import React from 'react';
import ReactDOM from 'react-dom/client';
import './assets/styles/index.css';
import reportWebVitals from './reportWebVitals';
import { PrimeReactProvider } from 'primereact/api';
import axios from 'axios';
import { BrowserRouter } from 'react-router-dom';
import ToastMessageServiceProvider from './app/providers/messages/toast-message-service.provider';
import { StoreProvider } from './app/providers/store-provider';
import App from './app/app.component';

axios.defaults.baseURL = import.meta.env.VITE_API_URL;
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';


const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement,
);

const queryClient = new QueryClient();
root.render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            <PrimeReactProvider>
                <ToastMessageServiceProvider>
                    <BrowserRouter>
                        <StoreProvider>
                            <App />
                        </StoreProvider>
                    </BrowserRouter>
                </ToastMessageServiceProvider>
            </PrimeReactProvider>
        </QueryClientProvider>
    </React.StrictMode>,
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
