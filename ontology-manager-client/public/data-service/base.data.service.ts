import { Observable, firstValueFrom } from "rxjs";
import { AxiosResponse } from "axios";

import { HttpResponsesHandlersFactory, IHttpResponsesHandler } from "./http-responses-handler";

export abstract class BaseDataService {
    protected constructor(protected handlersFactory: HttpResponsesHandlersFactory) {}

    protected handleRequest<TResult>(
        request: Observable<TResult>,
        responseHandler?: IHttpResponsesHandler
    ): Promise<TResult> {
        const promise = firstValueFrom(request);
        if (responseHandler) {
            return promise
                .then((response: TResult) => {
                    if (responseHandler.handleSuccess) {
                        responseHandler.handleSuccess();
                    }
                    return Promise.resolve(response);
                })
                .catch((errorResponse: AxiosResponse) => {
                    if (responseHandler.handleError) {
                        responseHandler.handleError(errorResponse);
                    }
                    return Promise.reject(errorResponse);
                });
        } else {
            return promise;
        }
    }

   
}
