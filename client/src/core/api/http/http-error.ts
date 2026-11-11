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