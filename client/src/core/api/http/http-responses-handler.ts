import { HttpStatusCodes } from './http-status-codes';
import { AxiosError } from 'axios';
import { MessageService } from '@/core/api/messages/interfaces/message-service';

export interface IHttpResponsesHandler {
    handleSuccess?(): void;
    handleError?(errorResponse: AxiosError): void;
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

    handleError(errorResponse: AxiosError): void {
        if (this.settings && this.settings.showErrorMessage == false) return;

        if (
            this.settings &&
            this.settings.errorStatusesMessages &&
            this.settings.errorStatusesMessages[
            errorResponse.response?.status
                ? errorResponse.response.status
                : 0
            ] != null
        ) {
            this.messageService.show({
                severity: 'error',
                summary: 'Error',
                detail: this.settings.errorStatusesMessages[
                    errorResponse.response?.status
                        ? errorResponse.response.status
                        : 0
                ],
            });
            return;
        }

        this.handleErrorHttpStatusCode(errorResponse);
    }

    protected handleErrorHttpStatusCode(errorResponse: AxiosError): void {
        switch (errorResponse.status) {
            default:
                this.handleOtherError(errorResponse);
        }
    }

    protected handleOtherError(errorResponse: AxiosError): void {
        this.messageService.show({
            severity: 'error',
            summary: 'Error',
            detail: errorResponse.code
                ? errorResponse.code
                : 'An error has occurred at the website and your support team will need to fix the problem. ' +
                'A preliminary report has been sent to the support team.Please do follow - up on the preliminary ' +
                'report using the Report a Problem page',
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
        errorResponse: AxiosError,
    ): void {
        if (
            errorResponse.response?.status == HttpStatusCodes.Status404NotFound
        ) {
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
