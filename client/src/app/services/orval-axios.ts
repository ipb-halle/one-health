import axios, { AxiosRequestConfig } from 'axios';
import { authService } from './auth.service';

// single reusable axios instance
const axiosInstance = axios.create({
    //baseURL: getApiBaseUrl(),
    //baseURL: import.meta.env.VITE_API_URL,
//    baseURL: 'http://a.localhost:5173/api',
    baseURL: '/api',
});


// Attach JWT automatically
axiosInstance.interceptors.request.use((config) => {
    const token = authService.getToken();

    if (token) {
        config.headers = config.headers ?? {};
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

export const customAxiosInstance = <T>(
    config: AxiosRequestConfig,
): Promise<T> => {
    return axiosInstance.request<T>(config).then(res => res.data);
};