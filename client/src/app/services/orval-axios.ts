import axios, { AxiosRequestConfig } from 'axios';

// single reusable axios instance
const axiosInstance = axios.create({
    baseURL: '/api',
    withXSRFToken: true,
    xsrfCookieName: 'XSRF-TOKEN',
    xsrfHeaderName: 'X-XSRF-TOKEN',
});

export const customAxiosInstance = <T>(
    config: AxiosRequestConfig,
): Promise<T> => {
    return axiosInstance.request<T>(config).then(res => res.data);
};