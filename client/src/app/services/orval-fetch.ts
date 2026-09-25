export class HttpError extends Error {
    constructor(
        public readonly status: number,
        public readonly response: Response,
        public readonly data: unknown,
    ) {
        super(`HTTP request failed with status ${status}`);
        this.name = 'HttpError';
    }
}

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

export const customFetch = async <T>(
    url: string,
    options: RequestInit,
): Promise<T> => {
    const headers = new Headers(options.headers);

    if (isUnsafeMethod(options.method)) {
        const csrfToken = getCookie('XSRF-TOKEN');

        if (csrfToken) {
            headers.set('X-XSRF-TOKEN', csrfToken);
        }
    }

    const response = await fetch(`/api${url}`, {
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
