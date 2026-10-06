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
    // 204 (No Content) and 205 (Reset Content) are successful responses
    // that do not contain a response body, so there is nothing to parse.
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

    // Spring Security requires a CSRF token for requests that can modify
    // server-side state (POST, PUT, PATCH, DELETE, etc.).
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

    // Read the response body before checking the status so that an error
    // response (e.g. 401, 403, 404 or 500) can also be included in HttpError.
    const data = await getResponseData(response);

    // response.ok accepts every successful 2xx status, not only 200.
    // Non-2xx statuses are preserved in HttpError for caller-specific handling.
    if (!response.ok) {
        throw new HttpError(response.status, response, data);
    }

    return data as T;
};