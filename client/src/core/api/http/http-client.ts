import { HttpError } from './http-error';

const baseUrl = import.meta.env.VITE_API_URL ?? '';

const getCookie = (name: string): string | undefined => {
    const prefix = `${encodeURIComponent(name)}=`;

    const cookie = document.cookie
        .split('; ')
        .find((item) => item.startsWith(prefix));

    return cookie
        ? decodeURIComponent(cookie.substring(prefix.length))
        : undefined;
};

const isUnsafeMethod = (method?: string): boolean => {
    const normalizedMethod = (method ?? 'GET').toUpperCase();

    return !['GET', 'HEAD', 'OPTIONS', 'TRACE'].includes(normalizedMethod);
};

const getResponseData = async (response: Response): Promise<unknown> => {
    if (response.status === 204 || response.status === 205) {
        return undefined;
    }

    const contentType = response.headers.get('content-type');

    if (contentType?.includes('application/json')) {
        return response.json();
    }

    const text = await response.text();

    return text.length > 0 ? text : undefined;
};

const getRequestUrl = (url: string): string => {
    const normalizedBaseUrl = baseUrl.endsWith('/')
        ? baseUrl.slice(0, -1)
        : baseUrl;

    const normalizedUrl = url.startsWith('/') ? url : `/${url}`;

    return `${normalizedBaseUrl}${normalizedUrl}`;
};

export const httpRequest = async <T>(
    url: string,
    options: RequestInit = {},
): Promise<T> => {
    const headers = new Headers(options.headers);

    if (isUnsafeMethod(options.method)) {
        const csrfToken = getCookie('XSRF-TOKEN');

        if (csrfToken) {
            headers.set('X-XSRF-TOKEN', csrfToken);
        }
    }

    const response = await fetch(getRequestUrl(url), {
        ...options,
        headers,
        credentials: 'same-origin',
    });

    const data = await getResponseData(response);

    if (!response.ok) {
        throw new HttpError(response.status, response, data);
    }

    return data as T;
};