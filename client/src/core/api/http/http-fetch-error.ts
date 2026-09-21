export class HttpFetchError extends Error {
  public status: number;
  public response: Response;

  constructor(response: Response, message?: string) {
    super(message ?? `HTTP Error: ${response.status}`);
    this.name = 'HttpFetchError';
    this.status = response.status;
    this.response = response;
  }
}
