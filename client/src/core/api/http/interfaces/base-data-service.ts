import { HttpError } from '../http-error';
import { IHttpResponsesHandler } from '../http-responses-handler';
import { injectable } from 'inversify';

/**
 * Provides basic functionality to handle an asynchronous request to a server
 */
@injectable()
export abstract class BaseDataService {
    readonly url: string = '';

    protected handleRequest<TResult>(
        request: Promise<TResult>,
        responseHandler?: IHttpResponsesHandler,
    ): Promise<TResult> {
        if (responseHandler) {
            return request
                .then((result: TResult) => {
                    if (responseHandler.handleSuccess) {
                        responseHandler.handleSuccess();
                    }
                    return result;
                })
                .catch((error: unknown) => {
                    if (
                        error instanceof HttpError &&
                        responseHandler.handleError
                    ) {
                        responseHandler.handleError(error);
                    }
                    return Promise.reject(error);
                });
        }
        return request;
    }
}
