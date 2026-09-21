export function httpFetch(
    endpoint: string,
    init?: RequestInit,
): Promise<Response> {
    const baseUrl = import.meta.env.VITE_API_URL ?? '';

    const resolvedUrl = (() => {
        if (/^https?:\/\//i.test(endpoint)) {
            return endpoint;
        }

        const normalizedBase = baseUrl.replace(/\/+$/, '');
        const normalizedEndpoint = endpoint.replace(/^\/+/, '');

        return `${normalizedBase}/${normalizedEndpoint}`;
    })();

    return fetch(resolvedUrl, init);
}
