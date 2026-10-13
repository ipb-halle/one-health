import { IHttpResponsesHandler } from '../http-responses-handler';
import { injectable } from 'inversify';

/**
 * Provides basic functionality to handle an asynchronous request to a server
 */
@injectable()
export abstract class BaseDataService {
    readonly url: string = '';

    protected async handleRequest<TResult>(
        request: Promise<TResult>,
        responseHandler?: IHttpResponsesHandler,
    ): Promise<TResult> {
        let result: TResult;

        try {
            result = await request;
        } catch (error: unknown) {
            responseHandler?.handleError?.(error);
            throw error;
        }

        responseHandler?.handleSuccess?.();
        return result;
    }
}
