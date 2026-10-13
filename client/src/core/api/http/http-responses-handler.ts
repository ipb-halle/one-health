import { HttpStatusCodes } from './http-status-codes';
import { HttpError } from './http-error';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

export interface IHttpResponsesHandler {
    handleSuccess?(): void;
    handleError?(errorResponse: unknown): void;
}

export interface IHttpResponseHandlerSettings {
    showSuccessMessage?: boolean; // Default false
    successMessage?: string;
    showErrorMessage?: boolean; // Default true
    errorMessage?: string;
    errorStatusesMessages?: { [key: number]: string };
}

export class BaseHttpResponsesHandler implements IHttpResponsesHandler {
    constructor(
        protected messageService: MessageService,
        protected settings?: IHttpResponseHandlerSettings,
    ) { }

    handleSuccess(): void {
        if (this.settings && this.settings.showSuccessMessage) {
            this.messageService.show({
                severity: 'success',
                summary: 'Success',
                detail: this.settings.successMessage
                    ? this.settings.successMessage
                    : 'Operation succeed.',
            });
        }
    }

    handleError(errorResponse: unknown): void {
        if (this.settings?.showErrorMessage === false) return;

        if (!(errorResponse instanceof HttpError)) {
            this.handleUnexpectedError(errorResponse);
            return;
        }

        const customMessage =
            this.settings?.errorStatusesMessages?.[errorResponse.status];

        if (customMessage != null) {
            this.messageService.show({
                severity: 'error',
                summary: 'Error',
                detail: customMessage,
            });
            return;
        }

        this.handleErrorHttpStatusCode(errorResponse);
    }

    protected handleErrorHttpStatusCode(errorResponse: HttpError): void {
        switch (errorResponse.status) {
            default:
                this.handleOtherError();
        }
    }

    protected handleOtherError(): void {
        this.messageService.show({
            severity: 'error',
            summary: 'Error',
            detail:
                'An error has occurred at the website and your support team will need to fix the problem. ' +
                'A preliminary report has been sent to the support team. Please do follow-up on the preliminary ' +
                'report using the Report a Problem page',
        });
    }

    protected handleUnexpectedError(error: unknown): void {
        if (error instanceof DOMException && error.name === 'AbortError') {
            return;
        }

        if (error instanceof TypeError) {
            this.messageService.show({
                severity: 'error',
                summary: 'Network Error',
                detail:
                    'Unable to complete the request. Please check your connection.',
            });
            return;
        }

        console.error('Unexpected request error:', error);

        this.messageService.show({
            severity: 'error',
            summary: 'Error',
            detail: 'An unexpected error occurred.',
        });
    }
}

export class OnReadByIdResponsesHandler extends BaseHttpResponsesHandler {
    constructor(
        private entityTitle: string,
        protected messageService: MessageService,
        settings?: IHttpResponseHandlerSettings,
    ) {
        super(messageService, settings);
    }

    protected override handleErrorHttpStatusCode(
        errorResponse: HttpError,
    ): void {
        if (errorResponse.status === HttpStatusCodes.Status404NotFound) {
            this.messageService.show({
                severity: 'error',
                summary: 'Error',
                detail:
                    `${this.entityTitle} not found. Perhaps another user has deleted the ${this.entityTitle}. ` +
                    'Please consider refreshing the page.',
            });
            return;
        }

        super.handleErrorHttpStatusCode(errorResponse);
    }
}

export class OnCreateResponseHandler extends BaseHttpResponsesHandler {
    constructor(
        entityTitle: string,
        protected messageService: MessageService,
        settings?: IHttpResponseHandlerSettings,
    ) {
        super(messageService, settings);

        if (!this.settings) this.settings = <IHttpResponseHandlerSettings>{};
        if (this.settings.showSuccessMessage == null)
            this.settings.showSuccessMessage = true;
        if (this.settings.successMessage == null)
            this.settings.successMessage = `${entityTitle} created.`;
    }
}
