import { IHttpResponsesHandler } from '../http-responses-handler';
import { HttpFetchError } from '../http-fetch-error';
import { injectable } from 'inversify';

/**
 * Provides basic functionality to handle an asynchronous request to a server
 */
@injectable()
export abstract class BaseDataService {
    readonly url: string = '';

    protected handleRequest<TResult>(
        request: Promise<Response>,
        responseHandler?: IHttpResponsesHandler,
    ): Promise<TResult> {
        return request
            .then(async (response: Response) => {
                if (!response.ok) {
                    throw new HttpFetchError(response);
                }

                const text = await response.text();
                const data = text ? JSON.parse(text) : null;

                if (responseHandler?.handleSuccess) {
                    responseHandler.handleSuccess();
                }

                return data as TResult;
            })
            .catch((error: HttpFetchError | Error) => {
                if (responseHandler?.handleError) {
                    responseHandler.handleError(error);
                }
                return Promise.reject(error);
            });
    }
}
